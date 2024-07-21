package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/DeferredImportSelector.class */
public interface DeferredImportSelector extends ImportSelector {
    @Nullable
    default Class<? extends Group> getImportGroup() {
        return null;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/DeferredImportSelector$Group.class */
    public interface Group {
        void process(AnnotationMetadata annotationMetadata, DeferredImportSelector deferredImportSelector);

        Iterable<Entry> selectImports();

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/DeferredImportSelector$Group$Entry.class */
        public static class Entry {
            private final AnnotationMetadata metadata;
            private final String importClassName;

            public Entry(AnnotationMetadata metadata, String importClassName) {
                this.metadata = metadata;
                this.importClassName = importClassName;
            }

            public AnnotationMetadata getMetadata() {
                return this.metadata;
            }

            public String getImportClassName() {
                return this.importClassName;
            }

            public boolean equals(@Nullable Object other) {
                if (this == other) {
                    return true;
                }
                if (other == null || getClass() != other.getClass()) {
                    return false;
                }
                Entry entry = (Entry) other;
                return this.metadata.equals(entry.metadata) && this.importClassName.equals(entry.importClassName);
            }

            public int hashCode() {
                return (this.metadata.hashCode() * 31) + this.importClassName.hashCode();
            }

            public String toString() {
                return this.importClassName;
            }
        }
    }
}
