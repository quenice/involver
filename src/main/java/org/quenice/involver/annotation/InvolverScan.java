package org.quenice.involver.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.quenice.involver.core.InvolverScannerRegistrar;

import java.lang.annotation.*;

/**
 * 手动代理对象扫描配置. 参考mybatis的实现方式MapperScan
 * @author damon.qiu 2020/10/26 4:32 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(InvolverScannerRegistrar.class)
public @interface InvolverScan {

    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified interface class as a parent.
     * <p>
     * Note this can be combined with annotationClass.
     */
    Class<?> markerInterface() default Class.class;
}
