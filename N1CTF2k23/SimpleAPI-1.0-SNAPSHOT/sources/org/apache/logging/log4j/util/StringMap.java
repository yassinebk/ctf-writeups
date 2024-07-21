package org.apache.logging.log4j.util;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/StringMap.class */
public interface StringMap extends ReadOnlyStringMap {
    void clear();

    boolean equals(Object obj);

    void freeze();

    int hashCode();

    boolean isFrozen();

    void putAll(ReadOnlyStringMap readOnlyStringMap);

    void putValue(String str, Object obj);

    void remove(String str);
}
