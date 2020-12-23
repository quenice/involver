package org.quenice.involver.handler;

import java.util.Date;

/**
 * 时间处理实现。
 * 由于在日志记录中需要获取到当前时间，所以可以选择性的实现这个handler，来自定义获取时间
 * @author damon.qiu 11/26/20 11:42 AM
 */
public interface TimeHandler {
    /**
     * 获得当前时间
     * @return
     */
    long getTime();

    /**
     * 获得当前时间
     * @return
     */
    Date getDate();
}
