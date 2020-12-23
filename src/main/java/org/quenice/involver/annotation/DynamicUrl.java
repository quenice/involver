package org.quenice.involver.annotation;

import org.springframework.core.annotation.AliasFor;
import org.quenice.involver.entity.UrlType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态url
 *
 * @author damon.qiu 2020/10/29 6:19 PM
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicUrl {
    UrlType value() default UrlType.BASE;

    /**
     * url类型。
     *
     * @return
     */
    @AliasFor("value")
    UrlType type() default UrlType.BASE;
}
