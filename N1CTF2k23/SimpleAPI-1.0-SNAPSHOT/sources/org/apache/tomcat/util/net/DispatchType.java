package org.apache.tomcat.util.net;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/DispatchType.class */
public enum DispatchType {
    NON_BLOCKING_READ(SocketEvent.OPEN_READ),
    NON_BLOCKING_WRITE(SocketEvent.OPEN_WRITE);
    
    private final SocketEvent status;

    DispatchType(SocketEvent status) {
        this.status = status;
    }

    public SocketEvent getSocketStatus() {
        return this.status;
    }
}
