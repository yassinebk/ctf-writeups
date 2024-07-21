package org.springframework.boot.availability;

import org.springframework.boot.availability.AvailabilityState;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/availability/AvailabilityChangeEvent.class */
public class AvailabilityChangeEvent<S extends AvailabilityState> extends ApplicationEvent {
    private final S state;

    public AvailabilityChangeEvent(Object source, S state) {
        super(source);
        Assert.notNull(state, "State must not be null");
        this.state = state;
    }

    public S getState() {
        return this.state;
    }

    public static <S extends AvailabilityState> void publish(ApplicationContext context, S state) {
        Assert.notNull(context, "Context must not be null");
        publish(context, context, state);
    }

    public static <S extends AvailabilityState> void publish(ApplicationEventPublisher publisher, Object source, S state) {
        Assert.notNull(publisher, "Publisher must not be null");
        publisher.publishEvent((ApplicationEvent) new AvailabilityChangeEvent(source, state));
    }
}
