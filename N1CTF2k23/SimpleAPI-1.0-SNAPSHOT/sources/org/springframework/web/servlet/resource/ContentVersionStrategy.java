package org.springframework.web.servlet.resource;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.resource.AbstractVersionStrategy;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/resource/ContentVersionStrategy.class */
public class ContentVersionStrategy extends AbstractVersionStrategy {
    public ContentVersionStrategy() {
        super(new AbstractVersionStrategy.FileNameVersionPathStrategy());
    }

    @Override // org.springframework.web.servlet.resource.VersionStrategy
    public String getResourceVersion(Resource resource) {
        try {
            byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return DigestUtils.md5DigestAsHex(content);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to calculate hash for " + resource, ex);
        }
    }
}
