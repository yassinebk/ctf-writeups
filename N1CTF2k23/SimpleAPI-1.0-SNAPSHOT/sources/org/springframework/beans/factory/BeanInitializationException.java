package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/BeanInitializationException.class */
public class BeanInitializationException extends FatalBeanException {
    public BeanInitializationException(String msg) {
        super(msg);
    }

    public BeanInitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
