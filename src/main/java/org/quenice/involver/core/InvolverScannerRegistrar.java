package org.quenice.involver.core;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import org.quenice.involver.annotation.InvolverScan;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态代理注册类
 * @author damon.qiu 2020/10/26 4:34 PM
 */
public class InvolverScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(InvolverScan.class.getName()));
        if (annoAttrs == null) return;

        List<String> basePackageList = new ArrayList<>();

        InvolverClassPathProxyBeanScanner scanner = new InvolverClassPathProxyBeanScanner(registry);

        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        // 设置扫描规则

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackageList.add(pkg);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackageList.add(pkg);
            }
        }

        String[] basePackages = StringUtils.toStringArray(basePackageList);

        // 设置扫描过滤规则
        scanner.registerFilters();
        // 开始扫描
        scanner.doScan(basePackages);
    }

}
