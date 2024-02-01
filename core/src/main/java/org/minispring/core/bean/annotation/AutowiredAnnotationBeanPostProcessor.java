package org.minispring.core.bean.annotation;

import org.minispring.core.bean.factory.BeanFactory;
import org.minispring.core.bean.factory.config.BeanPostProcessor;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Object result = bean;
        Class<?> clazz = bean.getClass();
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
