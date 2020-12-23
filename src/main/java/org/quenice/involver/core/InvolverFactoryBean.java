package org.quenice.involver.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理工厂Bean
 *
 * @author damon.qiu 2020/10/26 4:51 PM
 */
public class InvolverFactoryBean<T> implements FactoryBean<T> {
    private Class<T> interfaceType;
    private ApplicationContext applicationContext;

    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = new InvolverInvocationHandler(applicationContext);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class[]{interfaceType}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<T> getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
