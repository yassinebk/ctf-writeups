package org.springframework.format.datetime.joda;

import java.text.ParseException;
import java.util.Locale;
import org.joda.time.Period;
import org.springframework.format.Formatter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/datetime/joda/PeriodFormatter.class */
class PeriodFormatter implements Formatter<Period> {
    @Override // org.springframework.format.Parser
    public Period parse(String text, Locale locale) throws ParseException {
        return Period.parse(text);
    }

    @Override // org.springframework.format.Printer
    public String print(Period object, Locale locale) {
        return object.toString();
    }
}
