package org.springframework.objenesis;

import org.springframework.objenesis.instantiator.ObjectInstantiator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/objenesis/Objenesis.class */
public interface Objenesis {
    <T> T newInstance(Class<T> cls);

    <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> cls);
}
