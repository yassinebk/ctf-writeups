package org.springframework.boot.web.server;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/server/WebServer.class */
public interface WebServer {
    void start() throws WebServerException;

    void stop() throws WebServerException;

    int getPort();

    default void shutDownGracefully(GracefulShutdownCallback callback) {
        callback.shutdownComplete(GracefulShutdownResult.IMMEDIATE);
    }
}
