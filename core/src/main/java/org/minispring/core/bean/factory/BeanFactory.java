package org.minispring.core.bean.factory;

public interface BeanFactory {

    Object getBean(String name);

    boolean containsBean(String name);
}
