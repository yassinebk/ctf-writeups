package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/event/GenericApplicationListener.class */
public interface GenericApplicationListener extends ApplicationListener<ApplicationEvent>, Ordered {
    boolean supportsEventType(ResolvableType resolvableType);

    default boolean supportsSourceType(@Nullable Class<?> sourceType) {
        return true;
    }

    default int getOrder() {
        return Integer.MAX_VALUE;
    }
}
