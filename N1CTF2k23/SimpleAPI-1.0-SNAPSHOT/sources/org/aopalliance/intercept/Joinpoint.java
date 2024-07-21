package org.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/aopalliance/intercept/Joinpoint.class */
public interface Joinpoint {
    Object proceed() throws Throwable;

    Object getThis();

    AccessibleObject getStaticPart();
}
