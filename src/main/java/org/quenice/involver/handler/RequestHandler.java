package org.quenice.involver.handler;

import org.quenice.involver.annotation.Http;
import org.quenice.involver.entity.ExposedConfig;
import org.quenice.involver.entity.HttpMethod;

/**
 * 发起Http请求handler.
 *
 * @author damon.qiu 2020/10/28 5:24 PM
 * @since 1.0.0
 */
public interface RequestHandler {
    /**
     * http请求处理。
     *
     * @param url          请求url
     * @param requestParam 请求参数(已转JSON)。如果@Http中有配置codecHandler，那么这个参数是经过{@link CodecHandler#encode(String, ExposedConfig)}之后的值
     * @param httpMethod
     * @return
     * @see Http#codecHandler()
     * @see CodecHandler#encode(String, ExposedConfig) (String)
     * @since 1.0
     */
    String handle(String url, String requestParam, HttpMethod httpMethod, ExposedConfig exposedConfig);
}
