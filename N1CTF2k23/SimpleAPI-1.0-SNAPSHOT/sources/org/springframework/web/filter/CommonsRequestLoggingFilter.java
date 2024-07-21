package org.springframework.web.filter;

import javax.servlet.http.HttpServletRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/filter/CommonsRequestLoggingFilter.class */
public class CommonsRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected boolean shouldLog(HttpServletRequest request) {
        return this.logger.isDebugEnabled();
    }

    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void beforeRequest(HttpServletRequest request, String message) {
        this.logger.debug(message);
    }

    @Override // org.springframework.web.filter.AbstractRequestLoggingFilter
    protected void afterRequest(HttpServletRequest request, String message) {
        this.logger.debug(message);
    }
}
