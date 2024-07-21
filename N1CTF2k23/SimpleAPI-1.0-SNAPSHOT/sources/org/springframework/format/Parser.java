package org.springframework.format;

import java.text.ParseException;
import java.util.Locale;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/Parser.class */
public interface Parser<T> {
    T parse(String str, Locale locale) throws ParseException;
}
