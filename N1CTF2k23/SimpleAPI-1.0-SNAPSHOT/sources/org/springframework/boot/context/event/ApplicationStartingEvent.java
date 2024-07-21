package org.springframework.boot.context.event;

import org.springframework.boot.SpringApplication;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/event/ApplicationStartingEvent.class */
public class ApplicationStartingEvent extends SpringApplicationEvent {
    public ApplicationStartingEvent(SpringApplication application, String[] args) {
        super(application, args);
    }
}
