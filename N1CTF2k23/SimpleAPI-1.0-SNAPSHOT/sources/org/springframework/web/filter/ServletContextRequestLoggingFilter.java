package org.springframework.web.filter;

import javax.servlet.http.HttpServletRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/filter/ServletContextRequestLoggingFilter.class */
public class ServletContextRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void beforeRequest(HttpServletRequest request, String message) {
        getServletContext().log(message);
    }

    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void afterRequest(HttpServletRequest request, String message) {
        getServletContext().log(message);
    }
}
