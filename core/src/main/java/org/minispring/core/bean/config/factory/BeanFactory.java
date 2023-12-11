package org.minispring.core.bean.config.factory;

public interface BeanFactory {

    Object getBean(String name);

    boolean containsBean(String name);
}
