package org.springframework.core.type.classreading;

import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.MethodMetadata;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/classreading/SimpleMethodMetadata.class */
final class SimpleMethodMetadata implements MethodMetadata {
    private final String methodName;
    private final int access;
    private final String declaringClassName;
    private final String returnTypeName;
    private final MergedAnnotations annotations;

    public SimpleMethodMetadata(String methodName, int access, String declaringClassName, String returnTypeName, MergedAnnotations annotations) {
        this.methodName = methodName;
        this.access = access;
        this.declaringClassName = declaringClassName;
        this.returnTypeName = returnTypeName;
        this.annotations = annotations;
    }

    @Override // org.springframework.core.type.MethodMetadata
    public String getMethodName() {
        return this.methodName;
    }

    @Override // org.springframework.core.type.MethodMetadata
    public String getDeclaringClassName() {
        return this.declaringClassName;
    }

    @Override // org.springframework.core.type.MethodMetadata
    public String getReturnTypeName() {
        return this.returnTypeName;
    }

    @Override // org.springframework.core.type.MethodMetadata
    public boolean isAbstract() {
        return (this.access & 1024) != 0;
    }

    @Override // org.springframework.core.type.MethodMetadata
    public boolean isStatic() {
        return (this.access & 8) != 0;
    }

    @Override // org.springframework.core.type.MethodMetadata
    public boolean isFinal() {
        return (this.access & 16) != 0;
    }

    @Override // org.springframework.core.type.MethodMetadata
    public boolean isOverridable() {
        return (isStatic() || isFinal() || isPrivate()) ? false : true;
    }

    public boolean isPrivate() {
        return (this.access & 2) != 0;
    }

    @Override // org.springframework.core.type.AnnotatedTypeMetadata
    public MergedAnnotations getAnnotations() {
        return this.annotations;
    }
}
