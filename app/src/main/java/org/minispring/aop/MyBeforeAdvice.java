package org.minispring.aop;

import org.minispring.core.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;


public class MyBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("----------my interceptor befor method call----------");
    }

}
