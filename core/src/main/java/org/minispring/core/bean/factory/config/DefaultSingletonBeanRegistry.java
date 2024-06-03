package org.minispring.core.bean.factory.config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private final List<String> beanNames = new ArrayList<>();
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> dependenciesBeanMap = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>();

    @Override
    public void registerSingleton(@NonNull String beanName, @NonNull Object singletonObject) {
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (Objects.nonNull(oldObject)) {
                throw new IllegalStateException("Singleton object '" + beanName + "' already registered");
            }
            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
            log.info("bean registered: {}", beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        if(beanName == null) {
            return null;
        }
        return this.singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public java.lang.String[] getSingletonNames() {
        return beanNames.toArray(new String[0]);
    }

//    @Override
    public Object getSingletonMutex() {
        return this.singletonObjects;
    }

    public void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.beanNames.remove(beanName);
        }
    }

    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependenciesBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return (String[]) dependentBeans.toArray();
    }

    public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return (String[]) dependenciesForBean.toArray();
    }

    public void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = this.dependenciesBeanMap.get(beanName);
        if (!Objects.isNull(dependentBeans) && dependentBeans.contains(dependentBeanName)) {
            return;
        }

        synchronized (this.dependenciesBeanMap) {
            dependentBeans = this.dependenciesBeanMap.computeIfAbsent(beanName, k -> new LinkedHashSet<>());
            dependentBeans.add(dependentBeanName);
        }

        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet<>());
            dependenciesForBean.add(dependentBeanName);
        }
    }
}
