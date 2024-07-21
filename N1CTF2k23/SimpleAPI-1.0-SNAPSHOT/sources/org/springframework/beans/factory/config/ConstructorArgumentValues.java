package org.springframework.beans.factory.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.Mergeable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/config/ConstructorArgumentValues.class */
public class ConstructorArgumentValues {
    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap();
    private final List<ValueHolder> genericArgumentValues = new ArrayList();

    public ConstructorArgumentValues() {
    }

    public ConstructorArgumentValues(ConstructorArgumentValues original) {
        addArgumentValues(original);
    }

    public void addArgumentValues(@Nullable ConstructorArgumentValues other) {
        if (other != null) {
            other.indexedArgumentValues.forEach(index, argValue -> {
                addOrMergeIndexedArgumentValue(index, argValue.copy());
            });
            other.genericArgumentValues.stream().filter(valueHolder -> {
                return !this.genericArgumentValues.contains(valueHolder);
            }).forEach(valueHolder2 -> {
                addOrMergeGenericArgumentValue(valueHolder2.copy());
            });
        }
    }

    public void addIndexedArgumentValue(int index, @Nullable Object value) {
        addIndexedArgumentValue(index, new ValueHolder(value));
    }

    public void addIndexedArgumentValue(int index, @Nullable Object value, String type) {
        addIndexedArgumentValue(index, new ValueHolder(value, type));
    }

    public void addIndexedArgumentValue(int index, ValueHolder newValue) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        Assert.notNull(newValue, "ValueHolder must not be null");
        addOrMergeIndexedArgumentValue(Integer.valueOf(index), newValue);
    }

    private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue) {
        ValueHolder currentValue = this.indexedArgumentValues.get(key);
        if (currentValue != null && (newValue.getValue() instanceof Mergeable)) {
            Mergeable mergeable = (Mergeable) newValue.getValue();
            if (mergeable.isMergeEnabled()) {
                newValue.setValue(mergeable.merge(currentValue.getValue()));
            }
        }
        this.indexedArgumentValues.put(key, newValue);
    }

    public boolean hasIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.containsKey(Integer.valueOf(index));
    }

    @Nullable
    public ValueHolder getIndexedArgumentValue(int index, @Nullable Class<?> requiredType) {
        return getIndexedArgumentValue(index, requiredType, null);
    }

    @Nullable
    public ValueHolder getIndexedArgumentValue(int index, @Nullable Class<?> requiredType, @Nullable String requiredName) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = this.indexedArgumentValues.get(Integer.valueOf(index));
        if (valueHolder != null) {
            if (valueHolder.getType() == null || (requiredType != null && ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) {
                if (valueHolder.getName() == null || "".equals(requiredName) || (requiredName != null && requiredName.equals(valueHolder.getName()))) {
                    return valueHolder;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public Map<Integer, ValueHolder> getIndexedArgumentValues() {
        return Collections.unmodifiableMap(this.indexedArgumentValues);
    }

    public void addGenericArgumentValue(Object value) {
        this.genericArgumentValues.add(new ValueHolder(value));
    }

    public void addGenericArgumentValue(Object value, String type) {
        this.genericArgumentValues.add(new ValueHolder(value, type));
    }

    public void addGenericArgumentValue(ValueHolder newValue) {
        Assert.notNull(newValue, "ValueHolder must not be null");
        if (!this.genericArgumentValues.contains(newValue)) {
            addOrMergeGenericArgumentValue(newValue);
        }
    }

    private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
        if (newValue.getName() != null) {
            Iterator<ValueHolder> it = this.genericArgumentValues.iterator();
            while (it.hasNext()) {
                ValueHolder currentValue = it.next();
                if (newValue.getName().equals(currentValue.getName())) {
                    if (newValue.getValue() instanceof Mergeable) {
                        Mergeable mergeable = (Mergeable) newValue.getValue();
                        if (mergeable.isMergeEnabled()) {
                            newValue.setValue(mergeable.merge(currentValue.getValue()));
                        }
                    }
                    it.remove();
                }
            }
        }
        this.genericArgumentValues.add(newValue);
    }

    @Nullable
    public ValueHolder getGenericArgumentValue(Class<?> requiredType) {
        return getGenericArgumentValue(requiredType, null, null);
    }

    @Nullable
    public ValueHolder getGenericArgumentValue(Class<?> requiredType, String requiredName) {
        return getGenericArgumentValue(requiredType, requiredName, null);
    }

    @Nullable
    public ValueHolder getGenericArgumentValue(@Nullable Class<?> requiredType, @Nullable String requiredName, @Nullable Set<ValueHolder> usedValueHolders) {
        for (ValueHolder valueHolder : this.genericArgumentValues) {
            if (usedValueHolders == null || !usedValueHolders.contains(valueHolder)) {
                if (valueHolder.getName() == null || "".equals(requiredName) || (requiredName != null && valueHolder.getName().equals(requiredName))) {
                    if (valueHolder.getType() == null || (requiredType != null && ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) {
                        if (requiredType == null || valueHolder.getType() != null || valueHolder.getName() != null || ClassUtils.isAssignableValue(requiredType, valueHolder.getValue())) {
                            return valueHolder;
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<ValueHolder> getGenericArgumentValues() {
        return Collections.unmodifiableList(this.genericArgumentValues);
    }

    @Nullable
    public ValueHolder getArgumentValue(int index, Class<?> requiredType) {
        return getArgumentValue(index, requiredType, null, null);
    }

    @Nullable
    public ValueHolder getArgumentValue(int index, Class<?> requiredType, String requiredName) {
        return getArgumentValue(index, requiredType, requiredName, null);
    }

    @Nullable
    public ValueHolder getArgumentValue(int index, @Nullable Class<?> requiredType, @Nullable String requiredName, @Nullable Set<ValueHolder> usedValueHolders) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
        if (valueHolder == null) {
            valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
        }
        return valueHolder;
    }

    public int getArgumentCount() {
        return this.indexedArgumentValues.size() + this.genericArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.indexedArgumentValues.isEmpty() && this.genericArgumentValues.isEmpty();
    }

    public void clear() {
        this.indexedArgumentValues.clear();
        this.genericArgumentValues.clear();
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(@org.springframework.lang.Nullable java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            if (r0 != r1) goto L7
            r0 = 1
            return r0
        L7:
            r0 = r4
            boolean r0 = r0 instanceof org.springframework.beans.factory.config.ConstructorArgumentValues
            if (r0 != 0) goto L10
            r0 = 0
            return r0
        L10:
            r0 = r4
            org.springframework.beans.factory.config.ConstructorArgumentValues r0 = (org.springframework.beans.factory.config.ConstructorArgumentValues) r0
            r5 = r0
            r0 = r3
            java.util.List<org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r0 = r0.genericArgumentValues
            int r0 = r0.size()
            r1 = r5
            java.util.List<org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r1 = r1.genericArgumentValues
            int r1 = r1.size()
            if (r0 != r1) goto L3f
            r0 = r3
            java.util.Map<java.lang.Integer, org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r0 = r0.indexedArgumentValues
            int r0 = r0.size()
            r1 = r5
            java.util.Map<java.lang.Integer, org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r1 = r1.indexedArgumentValues
            int r1 = r1.size()
            if (r0 == r1) goto L41
        L3f:
            r0 = 0
            return r0
        L41:
            r0 = r3
            java.util.List<org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r0 = r0.genericArgumentValues
            java.util.Iterator r0 = r0.iterator()
            r6 = r0
            r0 = r5
            java.util.List<org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r0 = r0.genericArgumentValues
            java.util.Iterator r0 = r0.iterator()
            r7 = r0
        L56:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L8f
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L8f
            r0 = r6
            java.lang.Object r0 = r0.next()
            org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder r0 = (org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder) r0
            r8 = r0
            r0 = r7
            java.lang.Object r0 = r0.next()
            org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder r0 = (org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder) r0
            r9 = r0
            r0 = r8
            r1 = r9
            boolean r0 = org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder.access$000(r0, r1)
            if (r0 != 0) goto L8c
            r0 = 0
            return r0
        L8c:
            goto L56
        L8f:
            r0 = r3
            java.util.Map<java.lang.Integer, org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r0 = r0.indexedArgumentValues
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
            r8 = r0
        L9f:
            r0 = r8
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Lea
            r0 = r8
            java.lang.Object r0 = r0.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            r9 = r0
            r0 = r9
            java.lang.Object r0 = r0.getValue()
            org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder r0 = (org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder) r0
            r10 = r0
            r0 = r5
            java.util.Map<java.lang.Integer, org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder> r0 = r0.indexedArgumentValues
            r1 = r9
            java.lang.Object r1 = r1.getKey()
            java.lang.Object r0 = r0.get(r1)
            org.springframework.beans.factory.config.ConstructorArgumentValues$ValueHolder r0 = (org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder) r0
            r11 = r0
            r0 = r11
            if (r0 == 0) goto Le5
            r0 = r10
            r1 = r11
            boolean r0 = org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder.access$000(r0, r1)
            if (r0 != 0) goto Le7
        Le5:
            r0 = 0
            return r0
        Le7:
            goto L9f
        Lea:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.beans.factory.config.ConstructorArgumentValues.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int hashCode = 7;
        for (ValueHolder valueHolder : this.genericArgumentValues) {
            hashCode = (31 * hashCode) + valueHolder.contentHashCode();
        }
        int hashCode2 = 29 * hashCode;
        for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
            hashCode2 = (31 * hashCode2) + (entry.getValue().contentHashCode() ^ entry.getKey().hashCode());
        }
        return hashCode2;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/config/ConstructorArgumentValues$ValueHolder.class */
    public static class ValueHolder implements BeanMetadataElement {
        @Nullable
        private Object value;
        @Nullable
        private String type;
        @Nullable
        private String name;
        @Nullable
        private Object source;
        private boolean converted = false;
        @Nullable
        private Object convertedValue;

        public ValueHolder(@Nullable Object value) {
            this.value = value;
        }

        public ValueHolder(@Nullable Object value, @Nullable String type) {
            this.value = value;
            this.type = type;
        }

        public ValueHolder(@Nullable Object value, @Nullable String type, @Nullable String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        public void setValue(@Nullable Object value) {
            this.value = value;
        }

        @Nullable
        public Object getValue() {
            return this.value;
        }

        public void setType(@Nullable String type) {
            this.type = type;
        }

        @Nullable
        public String getType() {
            return this.type;
        }

        public void setName(@Nullable String name) {
            this.name = name;
        }

        @Nullable
        public String getName() {
            return this.name;
        }

        public void setSource(@Nullable Object source) {
            this.source = source;
        }

        @Override // org.springframework.beans.BeanMetadataElement
        @Nullable
        public Object getSource() {
            return this.source;
        }

        public synchronized boolean isConverted() {
            return this.converted;
        }

        public synchronized void setConvertedValue(@Nullable Object value) {
            this.converted = value != null;
            this.convertedValue = value;
        }

        @Nullable
        public synchronized Object getConvertedValue() {
            return this.convertedValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean contentEquals(ValueHolder other) {
            return this == other || (ObjectUtils.nullSafeEquals(this.value, other.value) && ObjectUtils.nullSafeEquals(this.type, other.type));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int contentHashCode() {
            return (ObjectUtils.nullSafeHashCode(this.value) * 29) + ObjectUtils.nullSafeHashCode(this.type);
        }

        public ValueHolder copy() {
            ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
            copy.setSource(this.source);
            return copy;
        }
    }
}
