package org.springframework.format;

import java.util.Locale;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/Printer.class */
public interface Printer<T> {
    String print(T t, Locale locale);
}
