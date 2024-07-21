package org.springframework.core.convert.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/IdToEntityConverter.class */
public final class IdToEntityConverter implements ConditionalGenericConverter {
    private final ConversionService conversionService;

    public IdToEntityConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
    }

    @Override // org.springframework.core.convert.converter.ConditionalConverter
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Method finder = getFinder(targetType.getType());
        return finder != null && this.conversionService.canConvert(sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    @Nullable
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Method finder = getFinder(targetType.getType());
        Assert.state(finder != null, "No finder method");
        Object id = this.conversionService.convert(source, sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
        return ReflectionUtils.invokeMethod(finder, source, id);
    }

    @Nullable
    private Method getFinder(Class<?> entityClass) {
        Method[] methods;
        boolean localOnlyFiltered;
        Method[] methodArr;
        String finderMethod = "find" + getEntityName(entityClass);
        try {
            methods = entityClass.getDeclaredMethods();
            localOnlyFiltered = true;
        } catch (SecurityException e) {
            methods = entityClass.getMethods();
            localOnlyFiltered = false;
        }
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getName().equals(finderMethod) && method.getParameterCount() == 1 && method.getReturnType().equals(entityClass) && (localOnlyFiltered || method.getDeclaringClass().equals(entityClass))) {
                return method;
            }
        }
        return null;
    }

    private String getEntityName(Class<?> entityClass) {
        String shortName = ClassUtils.getShortName(entityClass);
        int lastDot = shortName.lastIndexOf(46);
        if (lastDot != -1) {
            return shortName.substring(lastDot + 1);
        }
        return shortName;
    }
}
