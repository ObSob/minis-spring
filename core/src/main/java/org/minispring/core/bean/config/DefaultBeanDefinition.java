package org.minispring.core.bean.config;


public class DefaultBeanDefinition implements BeanDefinition {

    private boolean lazyInit = true;

    private final String className;

    public DefaultBeanDefinition(String className) {
        this.className = className;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public boolean isLazyInit() {
        return lazyInit;
    }



    @Override
    public String getBeanClassName() {
        return className;
    }
}
