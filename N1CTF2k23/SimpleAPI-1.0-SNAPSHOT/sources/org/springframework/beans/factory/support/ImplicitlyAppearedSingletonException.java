package org.springframework.beans.factory.support;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/ImplicitlyAppearedSingletonException.class */
class ImplicitlyAppearedSingletonException extends IllegalStateException {
    public ImplicitlyAppearedSingletonException() {
        super("About-to-be-created singleton instance implicitly appeared through the creation of the factory bean that its bean definition points to");
    }
}
