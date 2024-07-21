package org.slf4j.spi;

import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/slf4j-api-1.7.25.jar:org/slf4j/spi/MDCAdapter.class */
public interface MDCAdapter {
    void put(String str, String str2);

    String get(String str);

    void remove(String str);

    void clear();

    Map<String, String> getCopyOfContextMap();

    void setContextMap(Map<String, String> map);
}
