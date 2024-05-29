package org.minispring.core.bean.factory.config;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
public class PropertyValues implements Iterable<PropertyValue> {

    private List<PropertyValue> propertyValueList = new ArrayList<>();

    public PropertyValues(Map<String, Object> map) {
        this.propertyValueList = new ArrayList<PropertyValue>(10);
        for (Map.Entry<String, Object> e : map.entrySet()) {
            PropertyValue pv = new PropertyValue(e.getKey(), e.getValue());
            this.propertyValueList.add(pv);
        }
    }

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
