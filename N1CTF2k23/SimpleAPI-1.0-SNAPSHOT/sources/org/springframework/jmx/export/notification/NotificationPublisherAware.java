package org.springframework.jmx.export.notification;

import org.springframework.beans.factory.Aware;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/notification/NotificationPublisherAware.class */
public interface NotificationPublisherAware extends Aware {
    void setNotificationPublisher(NotificationPublisher notificationPublisher);
}
