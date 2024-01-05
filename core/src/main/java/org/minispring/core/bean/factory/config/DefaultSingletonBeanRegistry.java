package org.minispring.core.bean.factory.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public void registerSingleton(@NonNull String beanName, @NonNull Object singletonObject) {
        synchronized (this.singletonObjects) {
            if (singletonObjects.containsKey(beanName)) {
                throw new IllegalStateException("Singleton object '" + beanName + "' already registered");
            }
            singletonObjects.put(beanName, singletonObject);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public Object getSingletonMutex() {
        return this.singletonObjects;
    }
}
