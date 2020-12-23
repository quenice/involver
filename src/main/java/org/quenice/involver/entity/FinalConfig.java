package org.quenice.involver.entity;

import org.quenice.involver.handler.CodecHandler;
import org.quenice.involver.handler.LogHandler;
import org.quenice.involver.handler.RequestHandler;
import org.quenice.involver.handler.TimeHandler;

/**
 * 解析完后最终的配置
 *
 * @author damon.qiu 2020/10/27 5:24 PM
 */
public class FinalConfig {
    private ExposedConfig exposedConfig;
    private DynamicConfig dynamicConfig;
    private StaticConfig staticConfig;

    private String url;
    private Object param;
    private HttpMethod method;
    private boolean encrypt;
    private CodecHandler codecHandler;
    private RequestHandler requestHandler;
    private LogHandler logHandler;
    private TimeHandler timeHandler;

    public ExposedConfig getExposedConfig() {
        return exposedConfig;
    }

    public void setExposedConfig(ExposedConfig exposedConfig) {
        this.exposedConfig = exposedConfig;
    }

    public DynamicConfig getDynamicConfig() {
        return dynamicConfig;
    }

    public void setDynamicConfig(DynamicConfig dynamicConfig) {
        this.dynamicConfig = dynamicConfig;
    }

    public StaticConfig getStaticConfig() {
        return staticConfig;
    }

    public void setStaticConfig(StaticConfig staticConfig) {
        this.staticConfig = staticConfig;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

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
