package javax.websocket;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/MessageHandler.class */
public interface MessageHandler {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/MessageHandler$Partial.class */
    public interface Partial<T> extends MessageHandler {
        void onMessage(T t, boolean z);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/MessageHandler$Whole.class */
    public interface Whole<T> extends MessageHandler {
        void onMessage(T t);
    }
}
