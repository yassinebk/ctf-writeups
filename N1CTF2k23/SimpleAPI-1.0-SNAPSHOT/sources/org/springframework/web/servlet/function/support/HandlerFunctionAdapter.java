package org.springframework.web.servlet.function.support;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/support/HandlerFunctionAdapter.class */
public class HandlerFunctionAdapter implements HandlerAdapter, Ordered {
    private int order = Integer.MAX_VALUE;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    public boolean supports(Object handler) {
        return handler instanceof HandlerFunction;
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    @Nullable
    public ModelAndView handle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) throws Exception {
        HandlerFunction<?> handlerFunction = (HandlerFunction) handler;
        ServerRequest serverRequest = getServerRequest(servletRequest);
        ServerResponse serverResponse = handlerFunction.handle(serverRequest);
        return serverResponse.writeTo(servletRequest, servletResponse, new ServerRequestContext(serverRequest));
    }

    private ServerRequest getServerRequest(HttpServletRequest servletRequest) {
        ServerRequest serverRequest = (ServerRequest) servletRequest.getAttribute(RouterFunctions.REQUEST_ATTRIBUTE);
        Assert.state(serverRequest != null, () -> {
            return "Required attribute '" + RouterFunctions.REQUEST_ATTRIBUTE + "' is missing";
        });
        return serverRequest;
    }

    @Override // org.springframework.web.servlet.HandlerAdapter
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1L;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/support/HandlerFunctionAdapter$ServerRequestContext.class */
    private static class ServerRequestContext implements ServerResponse.Context {
        private final ServerRequest serverRequest;

        public ServerRequestContext(ServerRequest serverRequest) {
            this.serverRequest = serverRequest;
        }

        @Override // org.springframework.web.servlet.function.ServerResponse.Context
        public List<HttpMessageConverter<?>> messageConverters() {
            return this.serverRequest.messageConverters();
        }
    }
}
