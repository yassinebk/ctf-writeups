package org.springframework.core.convert.support;

import java.nio.charset.Charset;
import org.springframework.core.convert.converter.Converter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/StringToCharsetConverter.class */
class StringToCharsetConverter implements Converter<String, Charset> {
    @Override // org.springframework.core.convert.converter.Converter
    public Charset convert(String source) {
        return Charset.forName(source);
    }
}
