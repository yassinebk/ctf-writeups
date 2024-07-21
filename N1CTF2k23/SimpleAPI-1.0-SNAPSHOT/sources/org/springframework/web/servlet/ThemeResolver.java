package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/ThemeResolver.class */
public interface ThemeResolver {
    String resolveThemeName(HttpServletRequest httpServletRequest);

    void setThemeName(HttpServletRequest httpServletRequest, @Nullable HttpServletResponse httpServletResponse, @Nullable String str);
}
