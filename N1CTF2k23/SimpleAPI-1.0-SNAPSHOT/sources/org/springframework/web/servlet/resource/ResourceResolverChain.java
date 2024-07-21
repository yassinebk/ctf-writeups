package org.springframework.web.servlet.resource;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/resource/ResourceResolverChain.class */
public interface ResourceResolverChain {
    @Nullable
    Resource resolveResource(@Nullable HttpServletRequest httpServletRequest, String str, List<? extends Resource> list);

    @Nullable
    String resolveUrlPath(String str, List<? extends Resource> list);
}
