package org.springframework.web.servlet.resource;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/resource/ResourceTransformer.class */
public interface ResourceTransformer {
    Resource transform(HttpServletRequest httpServletRequest, Resource resource, ResourceTransformerChain resourceTransformerChain) throws IOException;
}
