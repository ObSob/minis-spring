package org.minispring.core.bean.config;

public interface BeanDefinition {

    void setLazyInit(boolean lazyInit);

    boolean isLazyInit();

    String getBeanClassName();
}
