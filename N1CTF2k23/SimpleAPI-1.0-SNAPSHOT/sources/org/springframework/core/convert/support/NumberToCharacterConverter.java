package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/NumberToCharacterConverter.class */
final class NumberToCharacterConverter implements Converter<Number, Character> {
    @Override // org.springframework.core.convert.converter.Converter
    public Character convert(Number source) {
        return Character.valueOf((char) source.shortValue());
    }
}
