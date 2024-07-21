package ch.qos.logback.core.net.server;

import java.io.Closeable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/net/server/Client.class */
public interface Client extends Runnable, Closeable {
    void close();
}
