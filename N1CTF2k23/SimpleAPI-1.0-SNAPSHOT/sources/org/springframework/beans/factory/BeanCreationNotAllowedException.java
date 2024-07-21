package org.springframework.beans.factory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/BeanCreationNotAllowedException.class */
public class BeanCreationNotAllowedException extends BeanCreationException {
    public BeanCreationNotAllowedException(String beanName, String msg) {
        super(beanName, msg);
    }
}
