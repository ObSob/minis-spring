package org.minispring.core.aop;

public interface Pointcut {
    MethodMatcher getMethodMatcher();
}