package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.util.StringValueResolver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/EmbeddedValueResolverAware.class */
public interface EmbeddedValueResolverAware extends Aware {
    void setEmbeddedValueResolver(StringValueResolver stringValueResolver);
}
