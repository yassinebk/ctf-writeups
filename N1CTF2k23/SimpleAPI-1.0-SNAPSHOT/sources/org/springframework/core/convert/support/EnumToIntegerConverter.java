package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/EnumToIntegerConverter.class */
final class EnumToIntegerConverter extends AbstractConditionalEnumConverter implements Converter<Enum<?>, Integer> {
    public EnumToIntegerConverter(ConversionService conversionService) {
        super(conversionService);
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Integer convert(Enum<?> source) {
        return Integer.valueOf(source.ordinal());
    }
}
