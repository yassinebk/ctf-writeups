package org.springframework.core.style;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.springframework.beans.PropertyAccessor;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/style/DefaultValueStyler.class */
public class DefaultValueStyler implements ValueStyler {
    private static final String EMPTY = "[[empty]]";
    private static final String NULL = "[null]";
    private static final String COLLECTION = "collection";
    private static final String SET = "set";
    private static final String LIST = "list";
    private static final String MAP = "map";
    private static final String EMPTY_MAP = "map[[empty]]";
    private static final String ARRAY = "array";

    @Override // org.springframework.core.style.ValueStyler
    public String style(@Nullable Object value) {
        if (value == null) {
            return NULL;
        }
        if (value instanceof String) {
            return "'" + value + "'";
        }
        if (value instanceof Class) {
            return ClassUtils.getShortName((Class) value);
        }
        if (value instanceof Method) {
            Method method = (Method) value;
            return method.getName() + "@" + ClassUtils.getShortName(method.getDeclaringClass());
        } else if (value instanceof Map) {
            return style((Map) value);
        } else {
            if (value instanceof Map.Entry) {
                return style((Map.Entry) value);
            }
            if (value instanceof Collection) {
                return style((Collection) value);
            }
            if (value.getClass().isArray()) {
                return styleArray(ObjectUtils.toObjectArray(value));
            }
            return String.valueOf(value);
        }
    }

    private <K, V> String style(Map<K, V> value) {
        if (value.isEmpty()) {
            return EMPTY_MAP;
        }
        StringJoiner result = new StringJoiner(", ", PropertyAccessor.PROPERTY_KEY_PREFIX, "]");
        for (Map.Entry<K, V> entry : value.entrySet()) {
            result.add(style((Map.Entry<?, ?>) entry));
        }
        return "map" + result;
    }

    private String style(Map.Entry<?, ?> value) {
        return style(value.getKey()) + " -> " + style(value.getValue());
    }

    private String style(Collection<?> value) {
        String collectionType = getCollectionTypeString(value);
        if (value.isEmpty()) {
            return collectionType + EMPTY;
        }
        StringJoiner result = new StringJoiner(", ", PropertyAccessor.PROPERTY_KEY_PREFIX, "]");
        for (Object o : value) {
            result.add(style(o));
        }
        return collectionType + result;
    }

    private String getCollectionTypeString(Collection<?> value) {
        if (value instanceof List) {
            return "list";
        }
        if (value instanceof Set) {
            return "set";
        }
        return COLLECTION;
    }

    private String styleArray(Object[] array) {
        if (array.length == 0) {
            return "array<" + ClassUtils.getShortName(array.getClass().getComponentType()) + '>' + EMPTY;
        }
        StringJoiner result = new StringJoiner(", ", PropertyAccessor.PROPERTY_KEY_PREFIX, "]");
        for (Object o : array) {
            result.add(style(o));
        }
        return "array<" + ClassUtils.getShortName(array.getClass().getComponentType()) + '>' + result;
    }
}
