package org.springframework.beans.factory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/BeanIsNotAFactoryException.class */
public class BeanIsNotAFactoryException extends BeanNotOfRequiredTypeException {
    public BeanIsNotAFactoryException(String name, Class<?> actualType) {
        super(name, FactoryBean.class, actualType);
    }
}
