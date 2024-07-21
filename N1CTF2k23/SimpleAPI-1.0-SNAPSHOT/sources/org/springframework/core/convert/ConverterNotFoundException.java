package org.springframework.core.convert;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/ConverterNotFoundException.class */
public class ConverterNotFoundException extends ConversionException {
    @Nullable
    private final TypeDescriptor sourceType;
    private final TypeDescriptor targetType;

    public ConverterNotFoundException(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
        super("No converter found capable of converting from type [" + sourceType + "] to type [" + targetType + "]");
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Nullable
    public TypeDescriptor getSourceType() {
        return this.sourceType;
    }

    public TypeDescriptor getTargetType() {
        return this.targetType;
    }
}
