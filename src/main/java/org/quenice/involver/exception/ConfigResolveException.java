package org.quenice.involver.exception;

/**
 * 配置解析异常
 * @author damon.qiu 2020/10/28 5:08 PM
 */
public class ConfigResolveException extends InvolverException {
    public ConfigResolveException(String msg) {
        super(msg);
    }

    public ConfigResolveException(String msg, Throwable e) {
        super(msg, e);
    }
}
