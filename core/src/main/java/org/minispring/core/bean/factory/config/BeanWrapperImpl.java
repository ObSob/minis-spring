package org.minispring.core.bean.factory.config;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    Object wrappedObject;
    Class<?> clz;
    PropertyValues propertyValues;

    public BeanWrapperImpl(Object object) {
        registerDefaultEditors();
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    public Object getBeanInstance() {
        return wrappedObject;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
        for (PropertyValue pv : this.propertyValues) {
            setPropertyValue(pv);
        }
    }

    public void setPropertyValue(PropertyValue pv) {
        BeanWrapperImpl.BeanPropertyHandler propertyHandler = new BeanWrapperImpl.BeanPropertyHandler(pv.getName());
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        }

        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }

     class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        @Getter
        Class<?> propertyClz = null;

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                this.writeMethod = clz.getDeclaredMethod("set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                // 获取读属性的方法，按照约定为getXxxx（）
                this.readMethod = clz.getDeclaredMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
