package org.springframework.format.datetime.standard;

import java.text.ParseException;
import java.time.Year;
import java.util.Locale;
import org.springframework.format.Formatter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/datetime/standard/YearFormatter.class */
class YearFormatter implements Formatter<Year> {
    @Override // org.springframework.format.Parser
    public Year parse(String text, Locale locale) throws ParseException {
        return Year.parse(text);
    }

    @Override // org.springframework.format.Printer
    public String print(Year object, Locale locale) {
        return object.toString();
    }
}
