package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/RequestToViewNameTranslator.class */
public interface RequestToViewNameTranslator {
    @Nullable
    String getViewName(HttpServletRequest httpServletRequest) throws Exception;
}
