package org.springframework.objenesis.instantiator.basic;

import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.instantiator.annotations.Instantiator;
import org.springframework.objenesis.instantiator.annotations.Typology;
import org.springframework.objenesis.instantiator.util.ClassUtils;
@Instantiator(Typology.NOT_COMPLIANT)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/objenesis/instantiator/basic/NewInstanceInstantiator.class */
public class NewInstanceInstantiator<T> implements ObjectInstantiator<T> {
    private final Class<T> type;

    public NewInstanceInstantiator(Class<T> type) {
        this.type = type;
    }

    @Override // org.springframework.objenesis.instantiator.ObjectInstantiator
    public T newInstance() {
        return (T) ClassUtils.newInstance(this.type);
    }
}
