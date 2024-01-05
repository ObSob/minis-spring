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
    private Boolean lazyInit;
    private String[] dependsOn;

    private ConstructorArgumentValues constructorArgumentValues;
    private PropertyValues propertyValues;
    private String[] initMethodNames;
    private String[] destroyMethodNames;

    private String id;
    private String className;
    private String scope = SCOPE_SINGLETON;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }
}
