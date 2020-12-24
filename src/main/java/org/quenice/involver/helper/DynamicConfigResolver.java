package org.quenice.involver.helper;

import org.quenice.involver.entity.DynamicConfig;
import org.quenice.involver.entity.StaticConfig;
import org.quenice.involver.exception.ConfigResolveException;

/**
 * 动态配置解析。
 * 这部分解析，在每次代理调用时，都会执行，
 * 但是由于繁杂耗时的解析都在{@link StaticConfigResolver}中做了并缓存了起来，
 * 所以这块的解析量非常小
 *
 * @author damon.qiu 12/22/20 5:31 PM
 * @see StaticConfigResolver
 * @since 1.0.0
 */
final class DynamicConfigResolver {


    /**
     * 解析动态配置
     *
     * @param staticConfig
     * @param args
     * @return
     * @throws ConfigResolveException
     */
    static DynamicConfig resolve(StaticConfig staticConfig, Object[] args) throws ConfigResolveException {
        DynamicConfig dynamicConfig = new DynamicConfig();
        handleUrls(dynamicConfig, staticConfig, args);
        handleParam(dynamicConfig, staticConfig, args);
        return dynamicConfig;
    }


    /**
     * 处理动态url
     *
     * @param dynamicConfig
     * @param staticConfig
     * @param args
     */
    private static void handleUrls(DynamicConfig dynamicConfig, StaticConfig staticConfig, Object[] args) {
        int[] indexs = staticConfig.getIndexs();
        if (staticConfig.isBaseUrlDynamic()) {
            dynamicConfig.setBaseUrl(args[indexs[ConfigResolver.INDEX_BASE_URL]].toString());
        }

        if (staticConfig.isSubUrlDynamic()) {
            dynamicConfig.setSubUrl(args[indexs[ConfigResolver.INDEX_SUB_URL]].toString());
        }
    }

    /**
     * 处理请求参数
     *
     * @param dynamicConfig
     * @param staticConfig
     * @param args
     */
    private static void handleParam(DynamicConfig dynamicConfig, StaticConfig staticConfig, Object[] args) {
        int indexOfParam = staticConfig.getIndexs()[ConfigResolver.INDEX_PARAM];
        if (indexOfParam > -1) {
            dynamicConfig.setParam(args[indexOfParam]);
        }
    }

}
