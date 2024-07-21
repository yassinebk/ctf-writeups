package org.springframework.context;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/ApplicationEventPublisher.class */
public interface ApplicationEventPublisher {
    void publishEvent(Object obj);

    default void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }
}
