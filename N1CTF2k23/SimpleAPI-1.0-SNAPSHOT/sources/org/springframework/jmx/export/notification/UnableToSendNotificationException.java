package org.springframework.jmx.export.notification;

import org.springframework.jmx.JmxException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/notification/UnableToSendNotificationException.class */
public class UnableToSendNotificationException extends JmxException {
    public UnableToSendNotificationException(String msg) {
        super(msg);
    }

    public UnableToSendNotificationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
