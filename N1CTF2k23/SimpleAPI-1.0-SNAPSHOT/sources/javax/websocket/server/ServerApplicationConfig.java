package javax.websocket.server;

import java.util.Set;
import javax.websocket.Endpoint;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/server/ServerApplicationConfig.class */
public interface ServerApplicationConfig {
    Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set);

    Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set);
}
