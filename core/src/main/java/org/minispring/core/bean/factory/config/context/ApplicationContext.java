package org.minispring.core.bean.factory.config.context;

import org.minispring.core.bean.exception.BeansException;
import org.minispring.core.bean.factory.ConfigurableBeanFactory;
import org.minispring.core.bean.factory.config.ConfigurableListableBeanFactory;
import org.minispring.core.bean.factory.config.ListableBeanFactory;

public interface ApplicationContext extends ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher{

    String getApplicationName();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();

}
