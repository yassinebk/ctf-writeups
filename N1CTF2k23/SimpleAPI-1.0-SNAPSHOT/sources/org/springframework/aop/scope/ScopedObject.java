package org.springframework.aop.scope;

import org.springframework.aop.RawTargetAccess;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/scope/ScopedObject.class */
public interface ScopedObject extends RawTargetAccess {
    Object getTargetObject();

    void removeFromScope();
}
