package org.minispring.core.bean.factory.config.context;

import org.minispring.core.bean.exception.BeansException;
import org.minispring.core.bean.factory.BeanFactory;

public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;

}
