package org.minispring.core.bean.factory.support;

import org.minispring.core.bean.factory.ConfigurableBeanFactory;
import org.minispring.core.bean.factory.config.AutowireCapableBeanFactory;
import org.minispring.core.bean.factory.config.ListableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

}
