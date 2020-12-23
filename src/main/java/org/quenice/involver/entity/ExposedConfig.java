package org.quenice.involver.entity;

/**
 * 暴露出的配置
 * @author damon.qiu 2020/10/29 3:47 PM
 */
public class ExposedConfig {
    private String url;
    private String classAdditional;
    private String methodAdditional;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassAdditional() {
        return classAdditional;
    }

    public void setClassAdditional(String classAdditional) {
        this.classAdditional = classAdditional;
    }

    public String getMethodAdditional() {
        return methodAdditional;
    }

    public void setMethodAdditional(String methodAdditional) {
        this.methodAdditional = methodAdditional;
    }

    @Override
    public ExposedConfig clone() {
        ExposedConfig exposedConfig = new ExposedConfig();
        exposedConfig.setUrl(this.url);
        exposedConfig.setClassAdditional(this.classAdditional);
        exposedConfig.setMethodAdditional(this.methodAdditional);
        return exposedConfig;
    }
}
