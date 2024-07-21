package org.apache.logging.log4j.spi;

import java.util.Map;
import org.apache.logging.log4j.util.StringMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/spi/ThreadContextMap2.class */
public interface ThreadContextMap2 extends ThreadContextMap {
    void putAll(Map<String, String> map);

    StringMap getReadOnlyContextData();
}
