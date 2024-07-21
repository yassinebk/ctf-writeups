package org.springframework.boot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ReflectionUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/SpringApplicationRunListeners.class */
public class SpringApplicationRunListeners {
    private final Log log;
    private final List<SpringApplicationRunListener> listeners;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpringApplicationRunListeners(Log log, Collection<? extends SpringApplicationRunListener> listeners) {
        this.log = log;
        this.listeners = new ArrayList(listeners);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void starting() {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.starting();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void environmentPrepared(ConfigurableEnvironment environment) {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.environmentPrepared(environment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void contextPrepared(ConfigurableApplicationContext context) {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.contextPrepared(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void contextLoaded(ConfigurableApplicationContext context) {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.contextLoaded(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void started(ConfigurableApplicationContext context) {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.started(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void running(ConfigurableApplicationContext context) {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.running(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        for (SpringApplicationRunListener listener : this.listeners) {
            callFailedListener(listener, context, exception);
        }
    }

    private void callFailedListener(SpringApplicationRunListener listener, ConfigurableApplicationContext context, Throwable exception) {
        try {
            listener.failed(context, exception);
        } catch (Throwable ex) {
            if (exception == null) {
                ReflectionUtils.rethrowRuntimeException(ex);
            }
            if (this.log.isDebugEnabled()) {
                this.log.error("Error handling failed", ex);
                return;
            }
            String message = ex.getMessage();
            this.log.warn("Error handling failed (" + (message != null ? message : "no error message") + ")");
        }
    }
}
