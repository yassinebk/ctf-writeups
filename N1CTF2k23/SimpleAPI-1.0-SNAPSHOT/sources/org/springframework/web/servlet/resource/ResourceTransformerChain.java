package org.springframework.web.servlet.resource;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/resource/ResourceTransformerChain.class */
public interface ResourceTransformerChain {
    ResourceResolverChain getResolverChain();

    Resource transform(HttpServletRequest httpServletRequest, Resource resource) throws IOException;
}
