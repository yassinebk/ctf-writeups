package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/event/EventExpressionRootObject.class */
class EventExpressionRootObject {
    private final ApplicationEvent event;
    private final Object[] args;

    public EventExpressionRootObject(ApplicationEvent event, Object[] args) {
        this.event = event;
        this.args = args;
    }

    public ApplicationEvent getEvent() {
        return this.event;
    }

    public Object[] getArgs() {
        return this.args;
    }
}
