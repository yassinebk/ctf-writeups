package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/support/ResourcePatternResolver.class */
public interface ResourcePatternResolver extends ResourceLoader {
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    Resource[] getResources(String str) throws IOException;
}
