package org.minispring.core.bean.factory.config;

public interface PropertyEditor {

    void setAsText(String text);

    void setValue(Object value);

    Object getValue();

    Object getAsText();

}