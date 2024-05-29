package org.minispring.core.bean.factory.config;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PropertyValue {

    private final String type;
    private final String name;
    private Object value;
    private final boolean isRef;

    public PropertyValue(String type, String name, Object value) {
        this(type, name, value, false);
    }

    public PropertyValue(String name, Object value) {
        this("", name, value, false);
    }


}
