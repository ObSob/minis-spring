package org.minispring.core.bean.factory.config;

import org.minispring.core.bean.factory.BeanFactory;

public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    void setBeanFactory(BeanFactory beanFactory);

}
