package org.springframework.web.servlet.handler;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/handler/SimpleServletHandlerAdapter.class */
public class SimpleServletHandlerAdapter implements HandlerAdapter {
    @Override // org.springframework.web.servlet.HandlerAdapter
    public boolean supports(Object handler) {
        return handler instanceof Servlet;
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    @Nullable
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ((Servlet) handler).service(request, response);
        return null;
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1L;
    }
}
