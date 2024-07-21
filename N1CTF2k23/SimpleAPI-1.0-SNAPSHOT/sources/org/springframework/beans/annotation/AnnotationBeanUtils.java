package org.springframework.beans.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringValueResolver;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/annotation/AnnotationBeanUtils.class */
public abstract class AnnotationBeanUtils {
    public static void copyPropertiesToBean(Annotation ann, Object bean, String... excludedProperties) {
        copyPropertiesToBean(ann, bean, null, excludedProperties);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void copyPropertiesToBean(Annotation ann, Object bean, @Nullable StringValueResolver valueResolver, String... excludedProperties) {
        Set<String> excluded = excludedProperties.length == 0 ? Collections.emptySet() : new HashSet<>(Arrays.asList(excludedProperties));
        Method[] annotationProperties = ann.annotationType().getDeclaredMethods();
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        for (Method annotationProperty : annotationProperties) {
            String propertyName = annotationProperty.getName();
            if (!excluded.contains(propertyName) && bw.isWritableProperty(propertyName)) {
                Object value = ReflectionUtils.invokeMethod(annotationProperty, ann);
                if (valueResolver != null && (value instanceof String)) {
                    value = valueResolver.resolveStringValue((String) value);
                }
                bw.setPropertyValue(propertyName, value);
            }
        }
    }
}
