package org.springframework.context.i18n;

import java.util.TimeZone;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/i18n/TimeZoneAwareLocaleContext.class */
public interface TimeZoneAwareLocaleContext extends LocaleContext {
    @Nullable
    TimeZone getTimeZone();
}
