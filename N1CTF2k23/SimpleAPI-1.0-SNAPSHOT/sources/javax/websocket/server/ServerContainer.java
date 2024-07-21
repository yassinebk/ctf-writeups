package javax.websocket.server;

import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/server/ServerContainer.class */
public interface ServerContainer extends WebSocketContainer {
    void addEndpoint(Class<?> cls) throws DeploymentException;

    void addEndpoint(ServerEndpointConfig serverEndpointConfig) throws DeploymentException;
}
