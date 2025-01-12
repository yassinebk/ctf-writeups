package org.springframework.core.type.filter;

import java.io.IOException;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/filter/AbstractClassTestingTypeFilter.class */
public abstract class AbstractClassTestingTypeFilter implements TypeFilter {
    protected abstract boolean match(ClassMetadata classMetadata);

    @Override // org.springframework.core.type.filter.TypeFilter
    public final boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return match(metadataReader.getClassMetadata());
    }
}
