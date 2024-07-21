package org.springframework.format.datetime.standard;

import java.text.ParseException;
import java.time.Duration;
import java.util.Locale;
import org.springframework.format.Formatter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/datetime/standard/DurationFormatter.class */
class DurationFormatter implements Formatter<Duration> {
    @Override // org.springframework.format.Parser
    public Duration parse(String text, Locale locale) throws ParseException {
        return Duration.parse(text);
    }

    @Override // org.springframework.format.Printer
    public String print(Duration object, Locale locale) {
        return object.toString();
    }
}
