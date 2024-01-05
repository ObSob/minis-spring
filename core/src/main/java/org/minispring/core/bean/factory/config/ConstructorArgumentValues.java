package org.minispring.core.bean.factory.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class ConstructorArgumentValues {

    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<>();

    private final List<ValueHolder> genericArgumentValues = new ArrayList<>();

    public void addGenericArgumentValue(Object value, String type) {
        addGenericArgumentValue(new ValueHolder(value, type));
    }

    public void addGenericArgumentValue(@NonNull ValueHolder newValue) {
        if(!this.genericArgumentValues.contains(newValue)) {
            this.genericArgumentValues.add(newValue);
        }
    }

    public boolean isEmpty() {
        return genericArgumentValues.isEmpty() && indexedArgumentValues.isEmpty();
    }

    public int getArgumentCount() {
        return genericArgumentValues.size() + indexedArgumentValues.size();
    }

    public ValueHolder getIndexedArgumentValue(int index) {
        return getIndexedArgumentValue(index, null, null);
    }

    private ValueHolder getIndexedArgumentValue(int index, Class<?> requiredType, String requiredName) {
        Preconditions.checkArgument(index >= 0);
        ValueHolder valueHolder = this.indexedArgumentValues.get(index);
        if (valueHolder != null &&
                (valueHolder.getType() == null || (requiredType != null &&
                        requiredType.equals(valueHolder.getType()))) &&
                (valueHolder.getName() == null || (requiredName != null &&
                        (requiredName.isEmpty() || requiredName.equals(valueHolder.getName()))))) {
            return valueHolder;
        }
        return null;
    }

    @Getter
    @Setter
    public static class ValueHolder {
        private Object value;
        private String type;
        private String name;
        private Object source;
        private boolean converted = false;
        private Object convertedValue;

        public ValueHolder(Object value, String type, String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        public ValueHolder(Object value, String type) {
            this.value = value;
            this.type = type;
        }
    }
}
