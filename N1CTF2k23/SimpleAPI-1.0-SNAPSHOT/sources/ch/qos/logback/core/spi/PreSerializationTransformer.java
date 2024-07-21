package ch.qos.logback.core.spi;

import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/spi/PreSerializationTransformer.class */
public interface PreSerializationTransformer<E> {
    Serializable transform(E e);
}
