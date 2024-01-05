package org.minispring.core.bean.factory.config.context;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void refresh();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

}
