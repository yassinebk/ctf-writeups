package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.io.ResourceLoader;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/ResourceLoaderAware.class */
public interface ResourceLoaderAware extends Aware {
    void setResourceLoader(ResourceLoader resourceLoader);
}
