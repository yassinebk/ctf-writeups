package org.springframework.boot.web.embedded.tomcat;

import org.springframework.boot.web.server.WebServerException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/ConnectorStartFailedException.class */
public class ConnectorStartFailedException extends WebServerException {
    private final int port;

    public ConnectorStartFailedException(int port) {
        super("Connector configured to listen on port " + port + " failed to start", null);
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }
}
