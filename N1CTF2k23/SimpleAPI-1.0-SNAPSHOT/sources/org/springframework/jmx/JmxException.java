package org.springframework.jmx;

import org.springframework.core.NestedRuntimeException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/JmxException.class */
public class JmxException extends NestedRuntimeException {
    public JmxException(String msg) {
        super(msg);
    }

    public JmxException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
