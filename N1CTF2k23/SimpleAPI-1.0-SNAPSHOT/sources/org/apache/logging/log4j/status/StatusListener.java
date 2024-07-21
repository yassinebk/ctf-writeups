package org.apache.logging.log4j.status;

import java.io.Closeable;
import java.util.EventListener;
import org.apache.logging.log4j.Level;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/status/StatusListener.class */
public interface StatusListener extends Closeable, EventListener {
    void log(StatusData statusData);

    Level getStatusLevel();
}
