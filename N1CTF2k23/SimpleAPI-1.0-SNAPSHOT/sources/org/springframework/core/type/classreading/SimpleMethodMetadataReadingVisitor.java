package org.springframework.core.type.classreading;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/classreading/SimpleMethodMetadataReadingVisitor.class */
final class SimpleMethodMetadataReadingVisitor extends MethodVisitor {
    @Nullable
    private final ClassLoader classLoader;
    private final String declaringClassName;
    private final int access;
    private final String name;
    private final String descriptor;
    private final List<MergedAnnotation<?>> annotations;
    private final Consumer<SimpleMethodMetadata> consumer;
    @Nullable
    private Source source;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleMethodMetadataReadingVisitor(@Nullable ClassLoader classLoader, String declaringClassName, int access, String name, String descriptor, Consumer<SimpleMethodMetadata> consumer) {
        super(17301504);
        this.annotations = new ArrayList(4);
        this.classLoader = classLoader;
        this.declaringClassName = declaringClassName;
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.consumer = consumer;
    }

    @Override // org.springframework.asm.MethodVisitor
    @Nullable
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        ClassLoader classLoader = this.classLoader;
        Supplier supplier = this::getSource;
        List<MergedAnnotation<?>> list = this.annotations;
        list.getClass();
        return MergedAnnotationReadingVisitor.get(classLoader, supplier, descriptor, visible, (v1) -> {
            r4.add(v1);
        });
    }

    @Override // org.springframework.asm.MethodVisitor
    public void visitEnd() {
        if (!this.annotations.isEmpty()) {
            String returnTypeName = Type.getReturnType(this.descriptor).getClassName();
            MergedAnnotations annotations = MergedAnnotations.of(this.annotations);
            SimpleMethodMetadata metadata = new SimpleMethodMetadata(this.name, this.access, this.declaringClassName, returnTypeName, annotations);
            this.consumer.accept(metadata);
        }
    }

    private Object getSource() {
        Source source = this.source;
        if (source == null) {
            source = new Source(this.declaringClassName, this.name, this.descriptor);
            this.source = source;
        }
        return source;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/classreading/SimpleMethodMetadataReadingVisitor$Source.class */
    static final class Source {
        private final String declaringClassName;
        private final String name;
        private final String descriptor;
        @Nullable
        private String toStringValue;

        Source(String declaringClassName, String name, String descriptor) {
            this.declaringClassName = declaringClassName;
            this.name = name;
            this.descriptor = descriptor;
        }

        public int hashCode() {
            int result = (31 * 1) + this.declaringClassName.hashCode();
            return (31 * ((31 * result) + this.name.hashCode())) + this.descriptor.hashCode();
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            Source otherSource = (Source) other;
            return this.declaringClassName.equals(otherSource.declaringClassName) && this.name.equals(otherSource.name) && this.descriptor.equals(otherSource.descriptor);
        }

        public String toString() {
            String value = this.toStringValue;
            if (value == null) {
                StringBuilder builder = new StringBuilder();
                builder.append(this.declaringClassName);
                builder.append(".");
                builder.append(this.name);
                Type[] argumentTypes = Type.getArgumentTypes(this.descriptor);
                builder.append("(");
                for (Type type : argumentTypes) {
                    builder.append(type.getClassName());
                }
                builder.append(")");
                value = builder.toString();
                this.toStringValue = value;
            }
            return value;
        }
    }
}
