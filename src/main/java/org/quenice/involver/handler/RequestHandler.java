package org.quenice.involver.handler;

import org.quenice.involver.annotation.Http;
import org.quenice.involver.entity.HttpMethod;

/**
 * Http发起请求handler.
 * 需要自己定义handler
 *
 * @author damon.qiu 2020/10/28 5:24 PM
 * @since 1.0
 */
public interface RequestHandler {
    /**
     * http请求处理。
     *
     * @param url          请求url
     * @param requestParam 请求参数(已转JSON)。如果@Http中有配置codecHandler，那么这个参数是经过{@link CodecHandler#encode(String)}之后的值
     * @param httpMethod
     * @return
     * @see Http#codecHandler()
     * @see CodecHandler#encode(String)
     * @since 1.0
     */
    String handle(String url, String requestParam, HttpMethod httpMethod);
}
