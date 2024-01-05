package org.minispring.core.bean.factory.config.context;

import org.minispring.core.bean.factory.BeanFactory;

public interface ApplicationContext extends BeanFactory {

    String getApplicationName();

}
