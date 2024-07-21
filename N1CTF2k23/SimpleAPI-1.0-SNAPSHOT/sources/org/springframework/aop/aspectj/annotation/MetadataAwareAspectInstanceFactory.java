package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/aspectj/annotation/MetadataAwareAspectInstanceFactory.class */
public interface MetadataAwareAspectInstanceFactory extends AspectInstanceFactory {
    AspectMetadata getAspectMetadata();

    @Nullable
    Object getAspectCreationMutex();
}
