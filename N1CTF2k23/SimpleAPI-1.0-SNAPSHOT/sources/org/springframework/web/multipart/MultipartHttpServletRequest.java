package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/multipart/MultipartHttpServletRequest.class */
public interface MultipartHttpServletRequest extends HttpServletRequest, MultipartRequest {
    @Nullable
    HttpMethod getRequestMethod();

    HttpHeaders getRequestHeaders();

    @Nullable
    HttpHeaders getMultipartHeaders(String str);
}
