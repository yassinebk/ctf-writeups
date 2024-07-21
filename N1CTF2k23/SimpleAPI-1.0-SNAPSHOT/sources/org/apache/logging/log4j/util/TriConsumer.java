package org.apache.logging.log4j.util;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/TriConsumer.class */
public interface TriConsumer<K, V, S> {
    void accept(K k, V v, S s);
}
