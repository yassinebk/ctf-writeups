package org.springframework.format.datetime.joda;

import java.util.Locale;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.Printer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/datetime/joda/ReadableInstantPrinter.class */
public final class ReadableInstantPrinter implements Printer<ReadableInstant> {
    private final DateTimeFormatter formatter;

    public ReadableInstantPrinter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override // org.springframework.format.Printer
    public String print(ReadableInstant instant, Locale locale) {
        return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(instant);
    }
}
