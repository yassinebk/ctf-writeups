package org.springframework.jmx.export.notification;

import javax.management.Notification;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/notification/NotificationPublisher.class */
public interface NotificationPublisher {
    void sendNotification(Notification notification) throws UnableToSendNotificationException;
}
