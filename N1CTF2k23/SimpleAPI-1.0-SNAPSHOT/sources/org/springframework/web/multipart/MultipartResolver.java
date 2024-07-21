package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/multipart/MultipartResolver.class */
public interface MultipartResolver {
    boolean isMultipart(HttpServletRequest httpServletRequest);

    MultipartHttpServletRequest resolveMultipart(HttpServletRequest httpServletRequest) throws MultipartException;

    void cleanupMultipart(MultipartHttpServletRequest multipartHttpServletRequest);
}
