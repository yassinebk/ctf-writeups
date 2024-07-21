package org.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerMapping;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/handler/MatchableHandlerMapping.class */
public interface MatchableHandlerMapping extends HandlerMapping {
    @Nullable
    RequestMatchResult match(HttpServletRequest httpServletRequest, String str);
}
