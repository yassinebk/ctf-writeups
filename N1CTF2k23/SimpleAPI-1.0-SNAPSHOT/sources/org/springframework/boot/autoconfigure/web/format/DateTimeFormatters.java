package org.springframework.boot.autoconfigure.web.format;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/format/DateTimeFormatters.class */
public class DateTimeFormatters {
    private DateTimeFormatter dateFormatter;
    private String datePattern;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateTimeFormatter;

    public DateTimeFormatters dateFormat(String pattern) {
        if (isIso(pattern)) {
            this.dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            this.datePattern = "yyyy-MM-dd";
        } else {
            this.dateFormatter = formatter(pattern);
            this.datePattern = pattern;
        }
        return this;
    }

    public DateTimeFormatters timeFormat(String pattern) {
        this.timeFormatter = isIso(pattern) ? DateTimeFormatter.ISO_LOCAL_TIME : formatter(pattern);
        return this;
    }

    public DateTimeFormatters dateTimeFormat(String pattern) {
        this.dateTimeFormatter = isIso(pattern) ? DateTimeFormatter.ISO_LOCAL_DATE_TIME : formatter(pattern);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DateTimeFormatter getDateFormatter() {
        return this.dateFormatter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDatePattern() {
        return this.datePattern;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DateTimeFormatter getTimeFormatter() {
        return this.timeFormatter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DateTimeFormatter getDateTimeFormatter() {
        return this.dateTimeFormatter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCustomized() {
        return (this.dateFormatter == null && this.timeFormatter == null && this.dateTimeFormatter == null) ? false : true;
    }

    private static DateTimeFormatter formatter(String pattern) {
        if (StringUtils.hasText(pattern)) {
            return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.SMART);
        }
        return null;
    }

    private static boolean isIso(String pattern) {
        return "iso".equalsIgnoreCase(pattern);
    }
}
