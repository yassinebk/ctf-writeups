package org.springframework.context;

import org.springframework.beans.factory.Aware;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/MessageSourceAware.class */
public interface MessageSourceAware extends Aware {
    void setMessageSource(MessageSource messageSource);
}