package org.springframework.web.servlet.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/handler/UserRoleAuthorizationInterceptor.class */
public class UserRoleAuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Nullable
    private String[] authorizedRoles;

    public final void setAuthorizedRoles(String... authorizedRoles) {
        this.authorizedRoles = authorizedRoles;
    }

    @Override // org.springframework.web.servlet.handler.HandlerInterceptorAdapter, org.springframework.web.servlet.HandlerInterceptor
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        String[] strArr;
        if (this.authorizedRoles != null) {
            for (String role : this.authorizedRoles) {
                if (request.isUserInRole(role)) {
                    return true;
                }
            }
        }
        handleNotAuthorized(request, response, handler);
        return false;
    }

    protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        response.sendError(403);
    }
}
