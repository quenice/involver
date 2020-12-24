package org.quenice.involver.handler;

import org.quenice.involver.exception.CodecException;

/**
 * Http请求参数/响应参数 加解密handler。
 * 如果有加解密需求，必须实现该类
 *
 * @author damon.qiu 2020/10/26 6:27 PM
 * @since 1.0.0
 */
public interface CodecHandler {
    /**
     * 编码（加密）
     *
     * @param requestParam 请求参数(JSON串)
     * @return 编码后的密文。这会是最终的请求数据
     * @throws CodecException
     */
    String encode(String requestParam) throws CodecException;

    /**
     * 解码（解密）
     *
     * @param responseResult 相应结果(JSON串)
     * @return 解码后的明文
     * @throws CodecException
     */
    String decode(String responseResult) throws CodecException;
}
