package org.springframework.web.context.request;

import java.io.Serializable;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/request/DestructionCallbackBindingListener.class */
public class DestructionCallbackBindingListener implements HttpSessionBindingListener, Serializable {
    private final Runnable destructionCallback;

    public DestructionCallbackBindingListener(Runnable destructionCallback) {
        this.destructionCallback = destructionCallback;
    }

    @Override // javax.servlet.http.HttpSessionBindingListener
    public void valueBound(HttpSessionBindingEvent event) {
    }

    @Override // javax.servlet.http.HttpSessionBindingListener
    public void valueUnbound(HttpSessionBindingEvent event) {
        this.destructionCallback.run();
    }
}
