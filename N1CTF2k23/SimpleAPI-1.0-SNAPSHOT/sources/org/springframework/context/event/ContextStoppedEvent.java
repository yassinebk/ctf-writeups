package org.springframework.context.event;

import org.springframework.context.ApplicationContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/event/ContextStoppedEvent.class */
public class ContextStoppedEvent extends ApplicationContextEvent {
    public ContextStoppedEvent(ApplicationContext source) {
        super(source);
    }
}
