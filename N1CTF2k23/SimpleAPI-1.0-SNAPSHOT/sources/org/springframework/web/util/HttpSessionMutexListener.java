package org.springframework.web.util;

import java.io.Serializable;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/HttpSessionMutexListener.class */
public class HttpSessionMutexListener implements HttpSessionListener {
    @Override // javax.servlet.http.HttpSessionListener
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE, new Mutex());
    }

    @Override // javax.servlet.http.HttpSessionListener
    public void sessionDestroyed(HttpSessionEvent event) {
        event.getSession().removeAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/HttpSessionMutexListener$Mutex.class */
    private static class Mutex implements Serializable {
        private Mutex() {
        }
    }
}
