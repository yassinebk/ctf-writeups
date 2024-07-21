package org.springframework.web.servlet.function.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/support/RouterFunctionMapping.class */
public class RouterFunctionMapping extends AbstractHandlerMapping implements InitializingBean {
    @Nullable
    private RouterFunction<?> routerFunction;
    private List<HttpMessageConverter<?>> messageConverters = Collections.emptyList();
    private boolean detectHandlerFunctionsInAncestorContexts = false;

    public RouterFunctionMapping() {
    }

    public RouterFunctionMapping(RouterFunction<?> routerFunction) {
        this.routerFunction = routerFunction;
    }

    public void setRouterFunction(@Nullable RouterFunction<?> routerFunction) {
        this.routerFunction = routerFunction;
    }

    @Nullable
    public RouterFunction<?> getRouterFunction() {
        return this.routerFunction;
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
    }

    public void setDetectHandlerFunctionsInAncestorContexts(boolean detectHandlerFunctionsInAncestorContexts) {
        this.detectHandlerFunctionsInAncestorContexts = detectHandlerFunctionsInAncestorContexts;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (this.routerFunction == null) {
            initRouterFunction();
        }
        if (CollectionUtils.isEmpty(this.messageConverters)) {
            initMessageConverters();
        }
    }

    private void initRouterFunction() {
        Map<String, RouterFunction> beansOfType;
        ApplicationContext applicationContext = obtainApplicationContext();
        if (this.detectHandlerFunctionsInAncestorContexts) {
            beansOfType = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RouterFunction.class);
        } else {
            beansOfType = applicationContext.getBeansOfType(RouterFunction.class);
        }
        Map<String, RouterFunction> beans = beansOfType;
        List<RouterFunction> routerFunctions = new ArrayList<>(beans.values());
        if (!CollectionUtils.isEmpty(routerFunctions) && this.logger.isInfoEnabled()) {
            routerFunctions.forEach(routerFunction -> {
                this.logger.info("Mapped " + routerFunction);
            });
        }
        this.routerFunction = routerFunctions.stream().reduce((v0, v1) -> {
            return v0.andOther(v1);
        }).orElse(null);
    }

    private void initMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>(4);
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        try {
            messageConverters.add(new SourceHttpMessageConverter<>());
        } catch (Error e) {
        }
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        this.messageConverters = messageConverters;
    }

    @Override // org.springframework.web.servlet.handler.AbstractHandlerMapping
    @Nullable
    protected Object getHandlerInternal(HttpServletRequest servletRequest) throws Exception {
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(servletRequest);
        servletRequest.setAttribute(LOOKUP_PATH, lookupPath);
        if (this.routerFunction != null) {
            ServerRequest request = ServerRequest.create(servletRequest, this.messageConverters);
            servletRequest.setAttribute(RouterFunctions.REQUEST_ATTRIBUTE, request);
            return this.routerFunction.route(request).orElse(null);
        }
        return null;
    }
}
