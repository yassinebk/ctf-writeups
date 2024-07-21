package org.springframework.core.convert.support;

import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.core.convert.converter.Converter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/support/ZoneIdToTimeZoneConverter.class */
final class ZoneIdToTimeZoneConverter implements Converter<ZoneId, TimeZone> {
    @Override // org.springframework.core.convert.converter.Converter
    public TimeZone convert(ZoneId source) {
        return TimeZone.getTimeZone(source);
    }
}
