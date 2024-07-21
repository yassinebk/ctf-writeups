package org.springframework.context;

import java.util.Locale;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/NoSuchMessageException.class */
public class NoSuchMessageException extends RuntimeException {
    public NoSuchMessageException(String code, Locale locale) {
        super("No message found under code '" + code + "' for locale '" + locale + "'.");
    }

    public NoSuchMessageException(String code) {
        super("No message found under code '" + code + "' for locale '" + Locale.getDefault() + "'.");
    }
}
