package org.springframework.jmx.export;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/UnableToRegisterMBeanException.class */
public class UnableToRegisterMBeanException extends MBeanExportException {
    public UnableToRegisterMBeanException(String msg) {
        super(msg);
    }

    public UnableToRegisterMBeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
