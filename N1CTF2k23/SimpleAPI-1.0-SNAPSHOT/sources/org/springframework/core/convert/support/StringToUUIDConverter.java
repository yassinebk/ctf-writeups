package org.springframework.core.convert.support;

import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/StringToUUIDConverter.class */
final class StringToUUIDConverter implements Converter<String, UUID> {
    @Override // org.springframework.core.convert.converter.Converter
    public UUID convert(String source) {
        if (StringUtils.hasText(source)) {
            return UUID.fromString(source.trim());
        }
        return null;
    }
}
