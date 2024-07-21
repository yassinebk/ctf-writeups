package org.springframework.cglib.transform.impl;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/transform/impl/InterceptFieldEnabled.class */
public interface InterceptFieldEnabled {
    void setInterceptFieldCallback(InterceptFieldCallback interceptFieldCallback);

    InterceptFieldCallback getInterceptFieldCallback();
}
