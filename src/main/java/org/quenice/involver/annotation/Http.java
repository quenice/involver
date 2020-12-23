package org.quenice.involver.annotation;

import org.springframework.core.annotation.AliasFor;
import org.quenice.involver.entity.Flag;
import org.quenice.involver.entity.HttpMethod;

import java.lang.annotation.*;

/**
 * HTTP请求配置。
 * 可以同时配置在类、方法上。如果方法上配置了，则会覆盖或者叠加，具体看每个属性说明
 *
 * @author damon.qiu 2020/10/26 6:19 PM
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Http {
    @AliasFor("url")
    String value() default "";

    /**
     * 请求URL。如果接口上也配置了, 则最终url = interface.url + method.url
     * <br>
     * 支持spring properties，如：${http.handler.rsp.base-url}
     *
     * @return
     */
    @AliasFor("value")
    String url() default "";

    /**
     * http method. default is POST
     * <br>
     * 方法会覆盖接口上配置
     *
     * @return
     */
    HttpMethod method() default HttpMethod.NONE;

    /**
     * 是否加密。如果为true，则会利用codecHandler中配置的bean去处理结果的加解密
     * <br>
     * 方法会覆盖接口
     *
     * @return
     */
    Flag codec() default Flag.NONE;

    /**
     * 加解密handler的bean name。注意：handler必须为HttpCodecHandler的实现类
     * <br>
     * 方法会覆盖接口
     *
     * @return
     */
    String codecHandler() default "";

    /**
     * 执行http请求的handler (bean name)。注意：handler必须为HttpRequestHandler的实现类
     * <br>
     * 方法会覆盖接口
     *
     * @return
     */
    String requestHandler() default "";

    /**
     * http日志handler (bean name)。注意：handler必须为HttpLogHandler的实现类
     * <br>
     * 方法会覆盖接口
     *
     * @return
     */
    String logHandler() default "";

    /**
     * 时间处理。
     * 注意：handler必须为TimeHandler的实现类。
     * 如果不配置这个handler，并且有实现了TimeHandler的spring bean，则使用该bean
     * @return
     */
    String timeHandler() default "";

    /**
     * 附加的数据。这个可以用于在各个handler中做个性化判断
     *
     * @return
     */
    String additional() default "";
}
