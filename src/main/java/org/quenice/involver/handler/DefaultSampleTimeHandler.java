package org.quenice.involver.handler;

import java.util.Date;

/**
 * 默认时间处理。
 * 当未配置TimeHandler时，默认使用这个handler
 *
 * @author damon.qiu 11/26/20 11:53 AM
 * @since 1.0.0
 */
public class DefaultSampleTimeHandler implements TimeHandler {

    private enum Singleton {
        INSTANCE;
        private DefaultSampleTimeHandler singleton;

        Singleton() {
            singleton = new DefaultSampleTimeHandler();
        }
    }

    private DefaultSampleTimeHandler() {
    }

    public static DefaultSampleTimeHandler getInstance() {
        return Singleton.INSTANCE.singleton;
    }

    @Override
    public long getTime() {

        return new Date().getTime();
    }

    @Override
    public Date getDate() {
        return new Date();
    }
}
