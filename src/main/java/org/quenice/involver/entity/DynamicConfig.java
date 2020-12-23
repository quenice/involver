package org.quenice.involver.entity;

/**
 * 动态的配置
 *
 * @author damon.qiu 12/22/20 11:50 AM
 */
public class DynamicConfig {
    private String baseUrl;
    private String subUrl;
    private Object param;

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

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
