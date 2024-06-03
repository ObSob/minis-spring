package org.minispring.core.bean.factory.support;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.minispring.core.aop.FactoryBean;
import org.minispring.core.aop.FactoryBeanRegistrySupport;
import org.minispring.core.bean.exception.BeansException;
import org.minispring.core.bean.factory.BeanFactory;
import org.minispring.core.bean.factory.BeanFactoryAware;
import org.minispring.core.bean.factory.ConfigurableBeanFactory;
import org.minispring.core.bean.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    protected List<String> beanDefinitionNames = new ArrayList<>();

    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    private BeanFactory parent;

    public void refresh() {
        for (String beanName : beanDefinitionMap.keySet()) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getBean(String beanName) throws BeansException {
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            // bean 为空时，需要先初始化
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                log.info("get bean {} null", beanName);
                BeanDefinition bd = beanDefinitionMap.get(beanName);
                if (bd != null) {
                    singleton = createBean(bd);
                    this.registerBean(beanName, singleton);
                    if (singleton instanceof BeanFactoryAware) {
                        ((BeanFactoryAware) singleton).setBeanFactory(this);
                    }

                    // step 1 : postProcessBeforeInitialization
                    singleton = applyBeanPostProcessorsBeforeInitialization(singleton, beanName);
                    //step 2 : init-method
                    if (bd.getInitMethodName() != null && !bd.getInitMethodName().isEmpty()) {
                        invokeInitMethod(bd, singleton);
                    }
                    //step 3 : postProcessAfterInitialization
                    applyBeanPostProcessorsAfterInitialization(singleton, beanName);
                    this.removeSingleton(beanName);
                    this.registerBean(beanName, singleton);
                }
            } else {
                return singleton;
            }
        }

        //处理 factoryBean
        if (singleton instanceof FactoryBean) {
            return this.getObjectForBeanInstance(singleton, beanName);
        }

        return singleton;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // Now we have the bean instance, which may be a normal bean or a FactoryBean.
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        Object object = null;
        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        object = getObjectFromFactoryBean(factory, beanName);
        return object;
    }

    private void invokeInitMethod(BeanDefinition bd, Object obj) {
        Class<?> clz = obj.getClass();
        Method method = null;
        try {
            method = clz.getMethod(bd.getInitMethodName());
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        try {
            assert method != null;
            method.invoke(obj);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object createBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = doCreateBean(bd);

        // 先创建毛坯类
        this.earlySingletonObjects.put(bd.getId(), obj);

        try {
            clz = Class.forName(bd.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        populateBean(bd, clz, obj);

        return obj;
    }

    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;

        try {
            clz = Class.forName(bd.getClassName());

            // 使用构造函数注入
            ConstructorArgumentValues argumentValues = bd.getConstructorArgumentValues();
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValues.ValueHolder argumentValue = argumentValues.getGenericArgumentValue(i);
                    String name = argumentValue.getName();

                    String type = argumentValue.getType();
                    if (String.class.getSimpleName().equals(type) || String.class.getName().equals(type)) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if (Integer.class.getSimpleName().equals(type) || Integer.class.getName().equals(type)) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if (beanDefinitionMap.containsKey(name)) {
                        paramTypes[i] = Class.forName(beanDefinitionMap.get(name).getClassName());
                        paramValues[i] = getBean(argumentValue.getName());
                    } else {
                        paramTypes[i] = Object.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try {
                    for (Constructor<?> c : clz.getConstructors()) {
                        if (c.getParameters().length != paramTypes.length) {
                            continue;
                        }
                        boolean match = true;
                        for (int i = 0; i < paramTypes.length; i++) {
                            if (!(c.getParameters()[i].getType().isAssignableFrom(paramTypes[i]))) {
                                match = false;
                                break;
                            }
                            if (i == paramTypes.length - 1) {
                                con = c;
                            }
                        }
                        if (!match) {
                            throw new RuntimeException("未找到符合符合条件的构造函数 " + clz.getName() + Arrays.toString(paramTypes));
                        }
                        obj = con.newInstance(paramValues);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                obj = clz.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("{} bean created. {} : {}", bd.getId(), bd.getClassName(), obj.toString());
        return obj;
    }

    private void populateBean(BeanDefinition bd, Class<?> clz, Object obj) {
        handleProperties(bd, clz, obj);
    }

    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
        //handle properties
        log.info("handle properties for bean : {}", bd.getId());
        PropertyValues propertyValues = bd.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.isRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) {
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }

                    paramValues[0] = pValue;
                } else { //is ref, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        paramValues[0] = getBean((String) pValue);

                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                }

                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);

                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    Objects.requireNonNull(method);
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    @Override
    public boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    public void registerBean(String beanName, Object singleton) {
        this.registerSingleton(beanName, singleton);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        this.beanDefinitionMap.put(name, bd);
        this.beanDefinitionNames.add(name);
        if (!bd.isLazyInit()) {
            Object bean = createBean(bd);
            registerBean(name, bean);
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    abstract public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    abstract public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
