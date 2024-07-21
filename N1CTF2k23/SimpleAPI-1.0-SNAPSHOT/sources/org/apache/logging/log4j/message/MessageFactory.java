package org.apache.logging.log4j.message;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/message/MessageFactory.class */
public interface MessageFactory {
    Message newMessage(Object obj);

    Message newMessage(String str);

    Message newMessage(String str, Object... objArr);
}
