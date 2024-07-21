package org.springframework.asm;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/asm/RecordComponentVisitor.class */
public abstract class RecordComponentVisitor {
    protected final int api;
    RecordComponentVisitor delegate;

    @Deprecated
    public RecordComponentVisitor(int api) {
        this(api, null);
    }

    @Deprecated
    public RecordComponentVisitor(int api, RecordComponentVisitor recordComponentVisitor) {
        if (api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17301504) {
            throw new IllegalArgumentException("Unsupported api " + api);
        }
        this.api = api;
        this.delegate = recordComponentVisitor;
    }

    @Deprecated
    public RecordComponentVisitor getDelegateExperimental() {
        return this.delegate;
    }

    @Deprecated
    public AnnotationVisitor visitAnnotationExperimental(String descriptor, boolean visible) {
        if (this.delegate != null) {
            return this.delegate.visitAnnotationExperimental(descriptor, visible);
        }
        return null;
    }

    @Deprecated
    public AnnotationVisitor visitTypeAnnotationExperimental(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        if (this.delegate != null) {
            return this.delegate.visitTypeAnnotationExperimental(typeRef, typePath, descriptor, visible);
        }
        return null;
    }

    @Deprecated
    public void visitAttributeExperimental(Attribute attribute) {
        if (this.delegate != null) {
            this.delegate.visitAttributeExperimental(attribute);
        }
    }

    @Deprecated
    public void visitEndExperimental() {
        if (this.delegate != null) {
            this.delegate.visitEndExperimental();
        }
    }
}
