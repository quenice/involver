package org.quenice.involver.entity;

import org.quenice.involver.handler.CodecHandler;
import org.quenice.involver.handler.LogHandler;
import org.quenice.involver.handler.RequestHandler;
import org.quenice.involver.handler.TimeHandler;

/**
 * 静态的配置
 *
 * @author damon.qiu 12/22/20 11:51 AM
 */
public class StaticConfig {
    private HttpMethod method;
    private boolean encrypt;
    private boolean baseUrlDynamic;
    private boolean subUrlDynamic;
    private String baseUrl;
    private String subUrl;
    /**
     * 第一位：参数index
     * 第二位：baseUrl index
     * 第三位：subUrl index
     */
    private int[] indexs;
    private CodecHandler codecHandler;
    private RequestHandler requestHandler;
    private LogHandler logHandler;
    private TimeHandler timeHandler;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public boolean isBaseUrlDynamic() {
        return baseUrlDynamic;
    }

    public void setBaseUrlDynamic(boolean baseUrlDynamic) {
        this.baseUrlDynamic = baseUrlDynamic;
    }

    public boolean isSubUrlDynamic() {
        return subUrlDynamic;
    }

    public void setSubUrlDynamic(boolean subUrlDynamic) {
        this.subUrlDynamic = subUrlDynamic;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public int[] getIndexs() {
        return indexs;
    }

    public void setIndexs(int[] indexs) {
        this.indexs = indexs;
    }

    public CodecHandler getCodecHandler() {
        return codecHandler;
    }

    public void setCodecHandler(CodecHandler codecHandler) {
        this.codecHandler = codecHandler;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public LogHandler getLogHandler() {
        return logHandler;
    }

    public void setLogHandler(LogHandler logHandler) {
        this.logHandler = logHandler;
    }

    public TimeHandler getTimeHandler() {
        return timeHandler;
    }

    public void setTimeHandler(TimeHandler timeHandler) {
        this.timeHandler = timeHandler;
    }
}
