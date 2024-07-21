package org.springframework.core.io;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/ResourceLoader.class */
public interface ResourceLoader {
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String str);

    @Nullable
    ClassLoader getClassLoader();
}
