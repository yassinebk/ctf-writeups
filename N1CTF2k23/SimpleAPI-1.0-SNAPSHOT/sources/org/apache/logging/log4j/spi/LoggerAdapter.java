package org.apache.logging.log4j.spi;

import java.io.Closeable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/spi/LoggerAdapter.class */
public interface LoggerAdapter<L> extends Closeable {
    L getLogger(String str);
}
