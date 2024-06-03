package org.minispring.core.aop;


import lombok.Getter;
import lombok.Setter;
import org.minispring.core.bean.exception.BeansException;
import org.minispring.core.bean.factory.BeanFactory;
import org.minispring.core.bean.factory.BeanFactoryAware;
import org.minispring.core.util.ClassUtils;

public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {
    @Setter
    private BeanFactory beanFactory;
    @Getter
    @Setter
    private AopProxyFactory aopProxyFactory;
    @Setter
    private String interceptorName;
    @Setter
    private String targetName;
    @Setter
    @Getter
    private Object target;
    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
    private Object singletonInstance;
    @Setter
    private PointcutAdvisor advisor;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }


    public ProxyFactoryBean(Object target, String interceptorName) {
        this.target = target;
        this.interceptorName = interceptorName;
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    @Override
    public Object getObject() throws Exception {
        initializeAdvisor();
        return getSingletonInstance();
    }

    private synchronized void initializeAdvisor() {
        Object advice = null;
//		MethodInterceptor mi = null;
        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        this.advisor = (PointcutAdvisor) advice;
//		if (advice instanceof BeforeAdvice) {
//			mi = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice)advice);
//		}
//		else if (advice instanceof AfterAdvice){
//			mi = new AfterReturningAdviceInterceptor((AfterReturningAdvice)advice);
//		}
//		else if (advice instanceof MethodInterceptor) {
//			mi = (MethodInterceptor)advice;
//		}

        //advisor = new NameMatchMethodPointcutAdvisor((Advice)advice);
        //advisor.setMethodInterceptor(mi);

    }

    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }

    protected AopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(target, this.advisor);
    }

    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

}
