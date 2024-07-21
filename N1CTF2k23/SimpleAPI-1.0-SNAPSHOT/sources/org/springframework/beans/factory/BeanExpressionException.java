package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/BeanExpressionException.class */
public class BeanExpressionException extends FatalBeanException {
    public BeanExpressionException(String msg) {
        super(msg);
    }

    public BeanExpressionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
