package org.aopalliance.intercept;

import java.lang.reflect.Constructor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/aopalliance/intercept/ConstructorInvocation.class */
public interface ConstructorInvocation extends Invocation {
    Constructor<?> getConstructor();
}
