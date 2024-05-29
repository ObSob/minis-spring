package org.minispring.core.bean.annotation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.minispring.core.bean.factory.BeanFactory;
import org.minispring.core.bean.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

@Getter
@Slf4j
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Object result = bean;
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                String fieldName = field.getName();
                Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                    log.info("autowire {} for bean {}", fieldName, beanName);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // todo
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
