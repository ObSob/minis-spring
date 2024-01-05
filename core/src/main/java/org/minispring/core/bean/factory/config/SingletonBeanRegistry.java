package org.minispring.core.bean.factory.config;

public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    Object getSingletonMutex();
}
