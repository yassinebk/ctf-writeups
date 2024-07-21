package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/net/server/ClientVisitor.class */
public interface ClientVisitor<T extends Client> {
    void visit(T t);
}
