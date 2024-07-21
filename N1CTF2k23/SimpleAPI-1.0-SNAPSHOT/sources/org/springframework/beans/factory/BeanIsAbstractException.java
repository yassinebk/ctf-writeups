package org.springframework.beans.factory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/BeanIsAbstractException.class */
public class BeanIsAbstractException extends BeanCreationException {
    public BeanIsAbstractException(String beanName) {
        super(beanName, "Bean definition is abstract");
    }
}
