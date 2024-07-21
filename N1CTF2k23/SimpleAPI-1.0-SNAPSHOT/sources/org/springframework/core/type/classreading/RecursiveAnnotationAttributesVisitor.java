package org.springframework.core.type.classreading;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/classreading/RecursiveAnnotationAttributesVisitor.class */
class RecursiveAnnotationAttributesVisitor extends AbstractRecursiveAnnotationVisitor {
    protected final String annotationType;

    public RecursiveAnnotationAttributesVisitor(String annotationType, AnnotationAttributes attributes, @Nullable ClassLoader classLoader) {
        super(classLoader, attributes);
        this.annotationType = annotationType;
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visitEnd() {
        AnnotationUtils.registerDefaultValues(this.attributes);
    }
}
