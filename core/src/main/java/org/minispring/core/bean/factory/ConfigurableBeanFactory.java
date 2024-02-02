package org.minispring.core.bean.factory;

import org.minispring.core.bean.factory.config.BeanPostProcessor;
import org.minispring.core.bean.factory.config.SingletonBeanRegistry;

public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

//    void setParentBeanFactory(BeanFactory parentBeanFactory);

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

//    void setBeanClassLoader(ClassLoader beanClassLoader);

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);

//    ClassLoader getBeanClassLoader();
}
