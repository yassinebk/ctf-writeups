package org.springframework.boot.availability;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/availability/ApplicationAvailabilityBean.class */
public class ApplicationAvailabilityBean implements ApplicationAvailability, ApplicationListener<AvailabilityChangeEvent<?>> {
    private final Map<Class<? extends AvailabilityState>, AvailabilityChangeEvent<?>> events = new HashMap();

    @Override // org.springframework.boot.availability.ApplicationAvailability
    public <S extends AvailabilityState> S getState(Class<S> stateType, S defaultState) {
        Assert.notNull(stateType, "StateType must not be null");
        Assert.notNull(defaultState, "DefaultState must not be null");
        S state = (S) getState(stateType);
        return state != null ? state : defaultState;
    }

    @Override // org.springframework.boot.availability.ApplicationAvailability
    public <S extends AvailabilityState> S getState(Class<S> stateType) {
        AvailabilityChangeEvent<S> event = getLastChangeEvent(stateType);
        if (event != null) {
            return event.getState();
        }
        return null;
    }

    @Override // org.springframework.boot.availability.ApplicationAvailability
    public <S extends AvailabilityState> AvailabilityChangeEvent<S> getLastChangeEvent(Class<S> stateType) {
        return (AvailabilityChangeEvent<S>) this.events.get(stateType);
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(AvailabilityChangeEvent<?> event) {
        Class<? extends AvailabilityState> stateType = getStateType(event.getState());
        this.events.put(stateType, event);
    }

    private Class<? extends AvailabilityState> getStateType(AvailabilityState state) {
        if (state instanceof Enum) {
            return ((Enum) state).getDeclaringClass();
        }
        return state.getClass();
    }
}
