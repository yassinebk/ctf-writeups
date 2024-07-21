package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/FactoryBeanNotInitializedException.class */
public class FactoryBeanNotInitializedException extends FatalBeanException {
    public FactoryBeanNotInitializedException() {
        super("FactoryBean is not fully initialized yet");
    }

    public FactoryBeanNotInitializedException(String msg) {
        super(msg);
    }
}
