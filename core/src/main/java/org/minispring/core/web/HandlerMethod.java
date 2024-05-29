package org.minispring.core.web;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Data
public class HandlerMethod {
    private Object bean;
    private  Class<?> beanType;
    private Method method;
    private Class<?> returnType;
    private String description;
    private String className;
    private String methodName;

    public HandlerMethod(Method method, Object obj) {
        this.setMethod(method);
        this.setBean(obj);
    }

    public HandlerMethod(Method method, Object obj, Class<?> clz, String methodName) {
        this.method = method;
        this.bean = obj;
        this.beanType = clz;
        this.methodName = methodName;

    }

}