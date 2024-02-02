package org.minispring.core.bean.factory.config;

import org.minispring.core.bean.factory.AbstractAutowireCapableBeanFactory;
import org.minispring.core.bean.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableFactory {

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();

        for (String beanName : this.beanDefinitionNames) {
            boolean matchFount = false;
            BeanDefinition bd = this.getBeanDefinition(beanName);
            Class<?> classToMatch = bd.getClass();
            if (type.isAssignableFrom(classToMatch)) {
                matchFount = true;
            } else {
                matchFount = false;
            }

            if (matchFount) {
                result.add(beanName);
            }
        }
        return result.toArray(new String[0]);
    }
}
