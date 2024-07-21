package org.apache.tomcat.websocket;

import javax.websocket.MessageHandler;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/WrappedMessageHandler.class */
public interface WrappedMessageHandler {
    long getMaxMessageSize();

    MessageHandler getWrappedHandler();
}
