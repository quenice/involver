package org.quenice.involver.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.quenice.involver.annotation.Http;
import org.quenice.involver.entity.DynamicConfig;
import org.quenice.involver.entity.ExposedConfig;
import org.quenice.involver.entity.FinalConfig;
import org.quenice.involver.entity.StaticConfig;
import org.quenice.involver.exception.ConfigResolveException;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配置解析器。
 * 动态代理时，会获取被代理接口上的{@link Http}配置和方法上的{@link Http}配置，
 * 需要通过这两个{@link Http}解析出所有的配置。
 *
 * <p>
 * 并且为了提升性能，减少不必要的开销，对静态的配置做了缓存处理。
 * </p>
 *
 * @author damon.qiu 2020/10/28 4:29 PM
 * @see Http
 * @since 1.0.0
 */
public class ConfigResolver {
    final static int INDEX_PARAM = 0;
    final static int INDEX_ADDITIONAL = 1;
    final static int INDEX_BASE_URL = 2;
    final static int INDEX_SUB_URL = 3;

    /**
     * 解析配置
     *
     * @param method
     * @param applicationContext
     * @return
     */
    public static FinalConfig resolve(Method method, Object[] args, ApplicationContext applicationContext) throws ConfigResolveException {
        // 获得类上http cofig
        Class<?> clazz = method.getDeclaringClass();
        Http classConfig = clazz.getAnnotation(Http.class);

        // 获得method上http config
        Http methodConfig = method.getAnnotation(Http.class);

        if (methodConfig == null)
            throw new ConfigResolveException("Annotation @Http on " + method.getClass() + " is Required");


        FinalConfig config = new FinalConfig();

        // 解析静态配置
        StaticConfig staticConfig = StaticConfigResolver.resolve(method, applicationContext);

        // 解析动态配置
        DynamicConfig dynamicConfig = DynamicConfigResolver.resolve(staticConfig, args);

        // 根据配置，计算出最终配置
        computeFinalConfig(config, staticConfig, dynamicConfig);

        // 设置暴露配置
        ExposedConfig exposedConfig = handleExposedConfig(config, staticConfig, dynamicConfig);

        config.setStaticConfig(staticConfig);
        config.setDynamicConfig(dynamicConfig);
        config.setExposedConfig(exposedConfig);

        return config;
    }

    /**
     * 根据解析出来的配置，计算出最终结果。这个提供给代理执行类辅助执行
     *
     * @param finalConfig
     * @param staticConfig
     * @param dynamicConfig
     */
    private static void computeFinalConfig(FinalConfig finalConfig, StaticConfig staticConfig, DynamicConfig dynamicConfig) {
        String baseUrl = staticConfig.isBaseUrlDynamic() ? dynamicConfig.getBaseUrl() : staticConfig.getBaseUrl();
        String subUrl = staticConfig.isSubUrlDynamic() ? dynamicConfig.getSubUrl() : staticConfig.getSubUrl();

        finalConfig.setUrl(baseUrl + subUrl);
        finalConfig.setEncrypt(staticConfig.isEncrypt());
        finalConfig.setMethod(staticConfig.getMethod());
        finalConfig.setParam(dynamicConfig.getParam());
        finalConfig.setCodecHandler(staticConfig.getCodecHandler());
        finalConfig.setLogHandler(staticConfig.getLogHandler());
        finalConfig.setRequestHandler(staticConfig.getRequestHandler());
        finalConfig.setTimeHandler(staticConfig.getTimeHandler());
    }


    /**
     * 处理暴露出去的config
     * @param config
     * @param staticConfig
     * @param dynamicConfig
     * @return
     */
    private static ExposedConfig handleExposedConfig(FinalConfig config, StaticConfig staticConfig, DynamicConfig dynamicConfig) {
        ExposedConfig exposedConfig = new ExposedConfig();
        exposedConfig.setUrl(config.getUrl());
        exposedConfig.setClassAdditional(staticConfig.getClassAdditional());
        exposedConfig.setMethodAdditional(staticConfig.getMethodAdditional());
        exposedConfig.setAdditionalParam(dynamicConfig.getAdditional());
        exposedConfig.setRawParam(dynamicConfig.getParam());
        return exposedConfig;
    }

    /**
     * 处理支持properties语法的属性
     *
     * @param key
     * @param applicationContext
     * @return
     */
    static String getPropertiesValue(String key, ApplicationContext applicationContext) {
        if (StringUtils.isEmpty(key)) return key;
        if (applicationContext == null) return key;

        String finalValue = key;
        Pattern p = Pattern.compile("\\$\\{(.*)}");
        Matcher matcher = p.matcher(key);
        while (matcher.find()) {
            String matchString = matcher.group();
            String matchGroup = matcher.group(1);

            String replace = applicationContext.getEnvironment().getProperty(matchGroup);
            if (StringUtils.isEmpty(replace)) {
                throw new ConfigResolveException("Can't find Property Key = " + matchGroup);
            }

            finalValue = finalValue.replace(matchString, replace);
        }

        return finalValue;
    }
}
