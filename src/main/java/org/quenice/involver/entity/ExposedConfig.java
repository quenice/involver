package org.quenice.involver.entity;

/**
 * 暴露出的配置
 *
 * @author damon.qiu 2020/10/29 3:47 PM
 */
public class ExposedConfig implements Cloneable {
    private String url;
    private Object additionalParam;
    private Object rawParam;
    private String classAdditional;
    private String methodAdditional;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getAdditionalParam() {
        return additionalParam;
    }

    public void setAdditionalParam(Object additionalParam) {
        this.additionalParam = additionalParam;
    }

    public Object getRawParam() {
        return rawParam;
    }

    public void setRawParam(Object rawParam) {
        this.rawParam = rawParam;
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
        exposedConfig.setRawParam(rawParam);
        exposedConfig.setAdditionalParam(additionalParam);
        exposedConfig.setClassAdditional(classAdditional);
        exposedConfig.setMethodAdditional(methodAdditional);
        exposedConfig.setUrl(url);
        return exposedConfig;
    }
}
