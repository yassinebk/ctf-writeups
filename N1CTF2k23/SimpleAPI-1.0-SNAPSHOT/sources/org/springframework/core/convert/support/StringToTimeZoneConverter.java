package org.springframework.core.convert.support;

import java.util.TimeZone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/StringToTimeZoneConverter.class */
class StringToTimeZoneConverter implements Converter<String, TimeZone> {
    @Override // org.springframework.core.convert.converter.Converter
    public TimeZone convert(String source) {
        return StringUtils.parseTimeZoneString(source);
    }
}
