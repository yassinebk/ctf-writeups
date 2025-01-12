package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/ObjectToOptionalConverter.class */
public final class ObjectToOptionalConverter implements ConditionalGenericConverter {
    private final ConversionService conversionService;

    public ObjectToOptionalConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        Set<GenericConverter.ConvertiblePair> convertibleTypes = new LinkedHashSet<>(4);
        convertibleTypes.add(new GenericConverter.ConvertiblePair(Collection.class, Optional.class));
        convertibleTypes.add(new GenericConverter.ConvertiblePair(Object[].class, Optional.class));
        convertibleTypes.add(new GenericConverter.ConvertiblePair(Object.class, Optional.class));
        return convertibleTypes;
    }

    @Override // org.springframework.core.convert.converter.ConditionalConverter
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (targetType.getResolvableType().hasGenerics()) {
            return this.conversionService.canConvert(sourceType, new GenericTypeDescriptor(targetType));
        }
        return true;
    }

    @Override // org.springframework.core.convert.converter.GenericConverter
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return Optional.empty();
        }
        if (source instanceof Optional) {
            return source;
        }
        if (targetType.getResolvableType().hasGenerics()) {
            Object target = this.conversionService.convert(source, sourceType, new GenericTypeDescriptor(targetType));
            if (target == null || ((target.getClass().isArray() && Array.getLength(target) == 0) || ((target instanceof Collection) && ((Collection) target).isEmpty()))) {
                return Optional.empty();
            }
            return Optional.of(target);
        }
        return Optional.of(source);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/ObjectToOptionalConverter$GenericTypeDescriptor.class */
    private static class GenericTypeDescriptor extends TypeDescriptor {
        public GenericTypeDescriptor(TypeDescriptor typeDescriptor) {
            super(typeDescriptor.getResolvableType().getGeneric(new int[0]), null, typeDescriptor.getAnnotations());
        }
    }
}
