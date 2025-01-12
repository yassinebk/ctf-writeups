package ch.qos.logback.core.spi;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.Status;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/spi/ContextAware.class */
public interface ContextAware {
    void setContext(Context context);

    Context getContext();

    void addStatus(Status status);

    void addInfo(String str);

    void addInfo(String str, Throwable th);

    void addWarn(String str);

    void addWarn(String str, Throwable th);

    void addError(String str);

    void addError(String str, Throwable th);
}
