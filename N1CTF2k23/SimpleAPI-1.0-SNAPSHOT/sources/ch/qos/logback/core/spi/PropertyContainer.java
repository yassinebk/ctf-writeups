package ch.qos.logback.core.spi;

import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/spi/PropertyContainer.class */
public interface PropertyContainer {
    String getProperty(String str);

    Map<String, String> getCopyOfPropertyMap();
}
