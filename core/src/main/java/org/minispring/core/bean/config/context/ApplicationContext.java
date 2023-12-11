package org.minispring.core.bean.config.context;

import org.minispring.core.bean.config.factory.BeanFactory;

public interface ApplicationContext extends BeanFactory {

    String getApplicationName();

}
