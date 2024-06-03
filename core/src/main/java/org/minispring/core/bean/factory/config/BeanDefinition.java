package org.minispring.core.bean.factory.config;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeanDefinition {
    private static final String SCOPE_DEFAULT = "";
    private static final String SCOPE_SINGLETON = "singleton";
    private static final String SCOPE_PROTOTYPE = "prototype";

    private volatile Object beanClass;
    private boolean lazyInit = true;
    private String[] dependsOn;

    private ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
    private PropertyValues propertyValues = new PropertyValues();
    private String[] initMethodNames;
    private String[] destroyMethodNames;

    private String id;
    private String className;
    private String scope = SCOPE_SINGLETON;
    private String initMethodName;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

}
