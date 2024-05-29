package org.minispring.core.bean.factory.config;

import lombok.Getter;
import lombok.Setter;

public class StringEditor implements PropertyEditor {
    private Class<?> strClass;
    private String strFormat;
    private boolean allowEmpty;
    @Getter
    @Setter
    private Object value;

    public StringEditor(Class<?> strClass, boolean allowEmpty) throws IllegalArgumentException {
        this(strClass, "", allowEmpty);
    }

    public StringEditor(Class<?> strClass, String strFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.strClass = strClass;
        this.strFormat = strFormat;
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text) {
        setValue(text);
    }

    public String getAsText() {
        return value.toString();
    }

}