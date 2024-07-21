package org.springframework.objenesis.strategy;

import org.springframework.objenesis.instantiator.ObjectInstantiator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/objenesis/strategy/InstantiatorStrategy.class */
public interface InstantiatorStrategy {
    <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> cls);
}
