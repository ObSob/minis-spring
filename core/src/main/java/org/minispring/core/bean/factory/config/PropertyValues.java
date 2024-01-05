package org.minispring.core.bean.factory.config;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class PropertyValues implements Iterable<PropertyValue> {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    @Override
    public Iterator<PropertyValue> iterator() {
        return propertyValueList.iterator();
    }

    public void addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }

    public boolean isEmpty() {
        return propertyValueList.isEmpty();
    }

    public int size() {
        return propertyValueList.size();
    }

}
