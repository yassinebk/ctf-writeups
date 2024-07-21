package org.springframework.context.annotation;

import org.springframework.core.type.AnnotatedTypeMetadata;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/Condition.class */
public interface Condition {
    boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata);
}
