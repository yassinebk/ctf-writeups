package org.springframework.core.type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/MethodMetadata.class */
public interface MethodMetadata extends AnnotatedTypeMetadata {
    String getMethodName();

    String getDeclaringClassName();

    String getReturnTypeName();

    boolean isAbstract();

    boolean isStatic();

    boolean isFinal();

    boolean isOverridable();
}
