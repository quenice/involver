package org.quenice.involver.annotation;

import org.quenice.involver.entity.UrlType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基URL
 * @author damon.qiu 12/22/20 11:12 AM
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@DynamicUrl(UrlType.BASE)
public @interface BaseUrl {
}
