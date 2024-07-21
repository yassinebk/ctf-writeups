package org.springframework.format.datetime.joda;

import java.text.ParseException;
import java.util.Locale;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.Parser;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/datetime/joda/LocalDateParser.class */
public final class LocalDateParser implements Parser<LocalDate> {
    private final DateTimeFormatter formatter;

    public LocalDateParser(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.format.Parser
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return JodaTimeContextHolder.getFormatter(this.formatter, locale).parseLocalDate(text);
    }
}
