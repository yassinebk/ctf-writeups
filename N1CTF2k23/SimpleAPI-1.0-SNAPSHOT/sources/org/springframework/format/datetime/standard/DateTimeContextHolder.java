package org.springframework.format.datetime.standard;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/datetime/standard/DateTimeContextHolder.class */
public final class DateTimeContextHolder {
    private static final ThreadLocal<DateTimeContext> dateTimeContextHolder = new NamedThreadLocal("DateTimeContext");

    private DateTimeContextHolder() {
    }

    public static void resetDateTimeContext() {
        dateTimeContextHolder.remove();
    }

    public static void setDateTimeContext(@Nullable DateTimeContext dateTimeContext) {
        if (dateTimeContext == null) {
            resetDateTimeContext();
        } else {
            dateTimeContextHolder.set(dateTimeContext);
        }
    }

    @Nullable
    public static DateTimeContext getDateTimeContext() {
        return dateTimeContextHolder.get();
    }

    public static DateTimeFormatter getFormatter(DateTimeFormatter formatter, @Nullable Locale locale) {
        DateTimeFormatter formatterToUse = locale != null ? formatter.withLocale(locale) : formatter;
        DateTimeContext context = getDateTimeContext();
        return context != null ? context.getFormatter(formatterToUse) : formatterToUse;
    }
}
