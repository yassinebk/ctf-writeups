package ch.qos.logback.core.net;

import java.util.concurrent.LinkedBlockingDeque;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/net/QueueFactory.class */
public class QueueFactory {
    public <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
        int actualCapacity = capacity < 1 ? 1 : capacity;
        return new LinkedBlockingDeque<>(actualCapacity);
    }
}
