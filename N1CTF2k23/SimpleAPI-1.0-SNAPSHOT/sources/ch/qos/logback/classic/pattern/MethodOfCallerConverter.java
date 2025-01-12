package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-classic-1.2.3.jar:ch/qos/logback/classic/pattern/MethodOfCallerConverter.class */
public class MethodOfCallerConverter extends ClassicConverter {
    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getMethodName();
        }
        return CallerData.NA;
    }
}
