package org.minispring.core.bean.config.processor;

import java.lang.reflect.Field;

import org.minispring.core.bean.config.annotation.Autowired;

public class AutowiredAnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Object result = null;
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) { //对每一个属性进行判断，如果带有@Autowired注解则进行处理
            for (Field field : fields) {
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if (isAutowired) { //根据属性名查找同名的bean
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    //设置属性值，完成注入
                    try {
                        field.setAccessible(true);
                        field.set(bean, autowiredObj);
                        System.out.println("autowire " + fieldName + " for bean " + beanName);
                    } catch (IllegalAccessException ignored) {

                    }
                }
            }
            return null;
        }


    }
}
    }

@Override
public Object postProcessAfterInitialization(Object bean,String beanName){
        return null;
        }
        }