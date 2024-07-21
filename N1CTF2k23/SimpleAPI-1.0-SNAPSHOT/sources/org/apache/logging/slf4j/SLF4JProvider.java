package org.apache.logging.slf4j;

import org.apache.logging.log4j.spi.Provider;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-to-slf4j-2.13.2.jar:org/apache/logging/slf4j/SLF4JProvider.class */
public class SLF4JProvider extends Provider {
    public SLF4JProvider() {
        super(15, "2.6.0", SLF4JLoggerContextFactory.class, MDCContextMap.class);
    }
}