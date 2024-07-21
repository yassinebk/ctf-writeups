package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/resource/VersionStrategy.class */
public interface VersionStrategy extends VersionPathStrategy {
    String getResourceVersion(Resource resource);
}
