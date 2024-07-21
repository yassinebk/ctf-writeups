package javax.websocket;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/Endpoint.class */
public abstract class Endpoint {
    public abstract void onOpen(Session session, EndpointConfig endpointConfig);

    public void onClose(Session session, CloseReason closeReason) {
    }

    public void onError(Session session, Throwable throwable) {
    }
}
