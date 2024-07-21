package org.springframework.jmx.export;

import org.springframework.jmx.JmxException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/MBeanExportException.class */
public class MBeanExportException extends JmxException {
    public MBeanExportException(String msg) {
        super(msg);
    }

    public MBeanExportException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
