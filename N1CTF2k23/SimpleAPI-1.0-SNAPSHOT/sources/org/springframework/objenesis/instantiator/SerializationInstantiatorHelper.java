package org.springframework.objenesis.instantiator;

import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/objenesis/instantiator/SerializationInstantiatorHelper.class */
public class SerializationInstantiatorHelper {
    public static <T> Class<? super T> getNonSerializableSuperClass(Class<T> type) {
        Class<T> cls = type;
        while (Serializable.class.isAssignableFrom(cls)) {
            cls = cls.getSuperclass();
            if (cls == null) {
                throw new Error("Bad class hierarchy: No non-serializable parents");
            }
        }
        return (Class<? super T>) cls;
    }
}
