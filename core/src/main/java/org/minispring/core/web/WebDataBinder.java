package org.minispring.core.web;

import jakarta.servlet.http.HttpServletRequest;
import org.minispring.core.bean.factory.config.BeanWrapperImpl;
import org.minispring.core.bean.factory.config.PropertyEditor;
import org.minispring.core.bean.factory.config.PropertyValues;
import org.minispring.core.util.WebUtils;

import java.util.Map;

public class WebDataBinder {

    private Object target;
    private Class<?> clz;
    private String objectName;
    private BeanWrapperImpl propertyAccessor;

    public WebDataBinder(Object target) {
        this(target, "");
    }

    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
        this.propertyAccessor = new BeanWrapperImpl(this.target);
    }

    //核心绑定方法，将request里面的参数值绑定到目标对象的属性上
    public void bind(HttpServletRequest request) {
        PropertyValues pvs = assignParameters(request);
        addBindValues(pvs, request);
        doBind(pvs);
    }

    private void doBind(PropertyValues pvs) {
        applyPropertyValues(pvs);
    }

    protected void applyPropertyValues(PropertyValues pvs) {
        getPropertyAccessor().setPropertyValues(pvs);
    }

    protected BeanWrapperImpl getPropertyAccessor() {
        return this.propertyAccessor;
    }

    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }

}
