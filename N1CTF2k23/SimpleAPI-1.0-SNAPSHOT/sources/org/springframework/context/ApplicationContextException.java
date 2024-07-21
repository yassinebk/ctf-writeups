package org.springframework.context;

import org.springframework.beans.FatalBeanException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/ApplicationContextException.class */
public class ApplicationContextException extends FatalBeanException {
    public ApplicationContextException(String msg) {
        super(msg);
    }

    public ApplicationContextException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
