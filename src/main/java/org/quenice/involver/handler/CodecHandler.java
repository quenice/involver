package org.quenice.involver.handler;

import org.quenice.involver.exception.CodecException;

/**
 * Http请求参数/响应参数 加解密handler。
 * 如果有加解密需求，必须实现该类
 *
 * @author damon.qiu 2020/10/26 6:27 PM
 */
public interface CodecHandler {
    /**
     * 编码
     *
     * @param requestParam 请求参数(JSON串)
     * @return
     * @throws CodecException
     */
    String encode(String requestParam) throws CodecException;

    /**
     * 解码
     *
     * @param responseResult 相应结果(JSON串)
     * @return
     * @throws CodecException
     */
    String decode(String responseResult) throws CodecException;
}
