package org.springframework.beans.factory.support;

import org.springframework.beans.FatalBeanException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/BeanDefinitionValidationException.class */
public class BeanDefinitionValidationException extends FatalBeanException {
    public BeanDefinitionValidationException(String msg) {
        super(msg);
    }

    public BeanDefinitionValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
