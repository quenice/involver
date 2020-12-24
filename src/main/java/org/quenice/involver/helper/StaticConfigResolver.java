package org.quenice.involver.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quenice.involver.annotation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.quenice.involver.entity.Flag;
import org.quenice.involver.entity.HttpMethod;
import org.quenice.involver.entity.StaticConfig;
import org.quenice.involver.entity.UrlType;
import org.quenice.involver.exception.ConfigResolveException;
import org.quenice.involver.handler.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 静态配置解析。
 * 静态配置解析完之后，会做缓存处理，防止无谓的重复解析，提高性能
 *
 * @author damon.qiu 12/22/20 5:31 PM
 * @since 1.0.0
 */
final class StaticConfigResolver {

    private final static Log logger = LogFactory.getLog(StaticConfigResolver.class);

    // 静态配置缓存
    private final static ConcurrentHashMap<String, StaticConfig> cacheConfigs = new ConcurrentHashMap<>(16);

    /**
     * 静态配置解析
     *
     * @param method
     * @param applicationContext
     * @return
     * @throws ConfigResolveException
     */
    static StaticConfig resolve(Method method, ApplicationContext applicationContext) throws ConfigResolveException {
        // 为了防止不同method的hashcode一致的情况，最好加上className和methodName
        String mapKey = method.getDeclaringClass().getName() + method.getName() + method.hashCode();
        // 缓存中拿静态配置
        StaticConfig config = cacheConfigs.get(mapKey);

        if (config != null) {
            logger.info("Get static-config from CACHE[" + mapKey + "]...");
            return config;
        }

        logger.info("Resolving static-config[" + mapKey + "]...");

        synchronized (StaticConfigResolver.class) {

            // 二次确认，防止重复解析
            config = cacheConfigs.get(mapKey);
            if (config != null) {
                logger.info("Get static-config from CACHE[" + mapKey + "]...");
                return config;
            }

            config = new StaticConfig();

            // 获得类上http cofig
            Class<?> clazz = method.getDeclaringClass();
            Http classConfig = clazz.getAnnotation(Http.class);

            // 获得method上http config
            Http methodConfig = method.getAnnotation(Http.class);

            config.setEncrypt(handleEncrypt(classConfig, methodConfig));
            config.setMethod(handleMethod(classConfig, methodConfig));

            // request handler
            config.setRequestHandler(handleRequestHandler(classConfig, methodConfig, applicationContext));

            // log handler
            config.setLogHandler(handleLogHandler(classConfig, methodConfig, applicationContext));

            // encrypt handler
            if (config.isEncrypt()) {
                config.setCodecHandler(handleEncryptHanlder(classConfig, methodConfig, applicationContext));
            }

            // time handler
            config.setTimeHandler(handleTimeHandler(classConfig, methodConfig, applicationContext));

            // 确定几个参数的位置
            config.setIndexs(locateArgs(method));

            // 确定是否动态URL
            config.setBaseUrlDynamic(config.getIndexs()[ConfigResolver.INDEX_BASE_URL] > -1);
            config.setSubUrlDynamic(config.getIndexs()[ConfigResolver.INDEX_SUB_URL] > -1);

            // 处理URL
            handleUrls(config, classConfig, methodConfig, applicationContext);

            // 处理additional
            handleStaticAdditional(config, classConfig, methodConfig);

            // 放缓存
            cacheConfigs.put(mapKey, config);
        }
        return config;
    }

    /**
     * 处理Http Method
     *
     * @param classConfig
     * @param methodConfig
     * @return
     */
    private static HttpMethod handleMethod(Http classConfig, Http methodConfig) {
        if (methodConfig.method() != HttpMethod.NONE) return methodConfig.method();
        if (classConfig == null) return HttpMethod.POST;
        if (classConfig.method() == HttpMethod.NONE) return HttpMethod.POST;
        return classConfig.method();
    }

    /**
     * 处理encrypt
     *
     * @param classConfig
     * @param methodConfig
     * @return
     */
    private static boolean handleEncrypt(Http classConfig, Http methodConfig) {
        if (methodConfig.codec() != Flag.NONE) return methodConfig.codec() == Flag.TRUE;
        if (classConfig != null) return classConfig.codec() == Flag.TRUE;
        return false;
    }

    /**
     * 处理codec handler
     *
     * @param classConfig
     * @param methodConfig
     * @param applicationContext
     * @return
     */
    private static CodecHandler handleEncryptHanlder(Http classConfig, Http methodConfig, ApplicationContext applicationContext) {
        String handlerBeanName = methodConfig.codecHandler();
        if (StringUtils.isEmpty(handlerBeanName) && classConfig != null) {
            handlerBeanName = classConfig.codecHandler();
        }

        if (StringUtils.isEmpty(handlerBeanName)) {
            throw new ConfigResolveException("encryptHandler is Required.");
        }

        try {
            return applicationContext.getBean(handlerBeanName, CodecHandler.class);
        } catch (Exception e) {
            throw new ConfigResolveException("Can't get bean of CodecHandler via bean name = " + handlerBeanName, e);
        }
    }

    /**
     * 处理request handler
     *
     * @param classConfig
     * @param methodConfig
     * @param applicationContext
     * @return
     */
    private static RequestHandler handleRequestHandler(Http classConfig, Http methodConfig, ApplicationContext applicationContext) {
        String handlerBeanName = methodConfig.requestHandler();
        if (StringUtils.isEmpty(handlerBeanName) && classConfig != null) {
            handlerBeanName = classConfig.requestHandler();
        }

        if (StringUtils.isEmpty(handlerBeanName)) {
            throw new ConfigResolveException("requestHandler is Required.");
        }

        try {
            return applicationContext.getBean(handlerBeanName, RequestHandler.class);
        } catch (Exception e) {
            throw new ConfigResolveException("Can't get bean of RequestHandler via bean name = " + handlerBeanName, e);
        }
    }

    /**
     * 处理log handler
     *
     * @param classConfig
     * @param methodConfig
     * @param applicationContext
     * @return
     */
    private static LogHandler handleLogHandler(Http classConfig, Http methodConfig, ApplicationContext applicationContext) {
        String handlerBeanName = methodConfig.logHandler();
        if (StringUtils.isEmpty(handlerBeanName) && classConfig != null) {
            handlerBeanName = classConfig.logHandler();
        }

        if (StringUtils.isEmpty(handlerBeanName)) {
            return null;
        }

        try {
            return applicationContext.getBean(handlerBeanName, LogHandler.class);
        } catch (Exception e) {
            throw new ConfigResolveException("Can't get bean of LogHandler via bean name = " + handlerBeanName, e);
        }
    }

    /**
     * 处理时间handler
     *
     * @param classConfig
     * @param methodConfig
     * @param applicationContext
     * @return
     */
    private static TimeHandler handleTimeHandler(Http classConfig, Http methodConfig, ApplicationContext applicationContext) {
        String handlerBeanName = methodConfig.timeHandler();
        if (StringUtils.isEmpty(handlerBeanName) && classConfig != null) {
            handlerBeanName = classConfig.timeHandler();
        }

        if (StringUtils.isEmpty(handlerBeanName)) {
            // 返回最基本默认实现
            return DefaultSampleTimeHandler.getInstance();
        }

        try {
            return applicationContext.getBean(handlerBeanName, TimeHandler.class);
        } catch (Exception e) {
            throw new ConfigResolveException("Can't get bean of TimeHandler via bean name = " + handlerBeanName, e);
        }
    }

    /**
     * 确定参数位置
     *
     * @param method
     * @return
     */
    private static int[] locateArgs(Method method) {

        Annotation[][] annoArr = method.getParameterAnnotations();
        if (annoArr == null || annoArr.length == 0) {
            return new int[]{-1, -1, -1};
        }

        int indexOfParam = -1;
        int indexOfAdditional = -1;
        int indexOfBaseUrl = -1;
        int indexOfSubUrl = -1;

        for (int i = 0; i < annoArr.length; i++) {
            Annotation[] pAnnos = annoArr[i];

            // 无annotation，即为输入参数
            if (pAnnos == null || pAnnos.length == 0) {
                if (indexOfParam == -1) indexOfParam = i;
                continue;
            }

            // 有annotation
            for (Annotation ann : pAnnos) {
                if (ann.annotationType() == BaseUrl.class) {
                    indexOfBaseUrl = i;
                    continue;
                }

                if (ann.annotationType() == SubUrl.class) {
                    indexOfSubUrl = i;
                    continue;
                }

                if (ann.annotationType() == DynamicUrl.class) {
                    DynamicUrl du = method.getAnnotation(DynamicUrl.class);
                    if (du.type() == UrlType.BASE) {
                        indexOfBaseUrl = i;
                    } else {
                        indexOfSubUrl = i;
                    }
                    continue;
                }

                if (ann.annotationType() == Additional.class) {
                    indexOfAdditional = i;
                    continue;
                }

                if (indexOfParam == -1) indexOfParam = i;
            }
        }

        return new int[]{indexOfParam, indexOfAdditional, indexOfBaseUrl, indexOfSubUrl};
    }

    /**
     * 处理url
     *
     * @param staticConfig
     * @param classConfig
     * @param methodConfig
     * @param applicationContext
     */
    private static void handleUrls(StaticConfig staticConfig, Http classConfig, Http methodConfig, ApplicationContext applicationContext) {
        if (!staticConfig.isBaseUrlDynamic()) {
            String staticClassUrl = classConfig == null ? "" : ((classConfig.url().equals("") ? classConfig.value() : classConfig.url()));
            staticConfig.setBaseUrl(ConfigResolver.getPropertiesValue(staticClassUrl, applicationContext));
        }

        if (!staticConfig.isSubUrlDynamic()) {
            String staticMehtodUrl = methodConfig.url().equals("") ? methodConfig.value() : methodConfig.url();
            staticConfig.setSubUrl(ConfigResolver.getPropertiesValue(staticMehtodUrl, applicationContext));
        }
    }

    /**
     * 处理静态的additional data
     *
     * @param staticConfig
     * @param classConfig
     * @param methodConfig
     */
    private static void handleStaticAdditional(StaticConfig staticConfig, Http classConfig, Http methodConfig) {
        staticConfig.setClassAdditional(classConfig == null ? null : classConfig.additional());
        staticConfig.setMethodAdditional(methodConfig.additional());
    }
}
