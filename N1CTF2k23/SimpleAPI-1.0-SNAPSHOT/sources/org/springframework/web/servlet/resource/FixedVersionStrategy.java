package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.AbstractVersionStrategy;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/resource/FixedVersionStrategy.class */
public class FixedVersionStrategy extends AbstractVersionStrategy {
    private final String version;

    public FixedVersionStrategy(String version) {
        super(new AbstractVersionStrategy.PrefixVersionPathStrategy(version));
        this.version = version;
    }

    @Override // org.springframework.web.servlet.resource.VersionStrategy
    public String getResourceVersion(Resource resource) {
        return this.version;
    }
}
