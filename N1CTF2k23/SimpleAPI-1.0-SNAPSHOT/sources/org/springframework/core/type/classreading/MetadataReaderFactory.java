package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/classreading/MetadataReaderFactory.class */
public interface MetadataReaderFactory {
    MetadataReader getMetadataReader(String str) throws IOException;

    MetadataReader getMetadataReader(Resource resource) throws IOException;
}
