package org.springframework.jmx;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/MBeanServerNotFoundException.class */
public class MBeanServerNotFoundException extends JmxException {
    public MBeanServerNotFoundException(String msg) {
        super(msg);
    }

    public MBeanServerNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
