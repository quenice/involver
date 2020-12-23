package org.quenice.involver.handler;

import org.quenice.involver.entity.ExposedConfig;
import org.quenice.involver.entity.RequestLog;

/**
 * 日志处理器
 *
 * @author damon.qiu 2020/10/28 6:18 PM
 */
public interface LogHandler {

    /**
     * 日志处理
     *
     * @param requestLog    http请求过程日志
     * @param exposedConfig 暴露出的配置信息
     */
    void handle(RequestLog requestLog, ExposedConfig exposedConfig);
}
