package org.springframework.web.servlet.mvc.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/RequestMethodsRequestCondition.class */
public final class RequestMethodsRequestCondition extends AbstractRequestCondition<RequestMethodsRequestCondition> {
    private static final Map<String, RequestMethodsRequestCondition> requestMethodConditionCache = new HashMap(RequestMethod.values().length);
    private final Set<RequestMethod> methods;

    static {
        RequestMethod[] values;
        for (RequestMethod method : RequestMethod.values()) {
            requestMethodConditionCache.put(method.name(), new RequestMethodsRequestCondition(method));
        }
    }

    public RequestMethodsRequestCondition(RequestMethod... requestMethods) {
        this(Arrays.asList(requestMethods));
    }

    private RequestMethodsRequestCondition(Collection<RequestMethod> requestMethods) {
        this.methods = Collections.unmodifiableSet(new LinkedHashSet(requestMethods));
    }

    public Set<RequestMethod> getMethods() {
        return this.methods;
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    protected Collection<RequestMethod> getContent() {
        return this.methods;
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    protected String getToStringInfix() {
        return " || ";
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public RequestMethodsRequestCondition combine(RequestMethodsRequestCondition other) {
        Set<RequestMethod> set = new LinkedHashSet<>(this.methods);
        set.addAll(other.methods);
        return new RequestMethodsRequestCondition(set);
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    @Nullable
    public RequestMethodsRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return matchPreFlight(request);
        }
        if (getMethods().isEmpty()) {
            if (RequestMethod.OPTIONS.name().equals(request.getMethod()) && !DispatcherType.ERROR.equals(request.getDispatcherType())) {
                return null;
            }
            return this;
        }
        return matchRequestMethod(request.getMethod());
    }

    @Nullable
    private RequestMethodsRequestCondition matchPreFlight(HttpServletRequest request) {
        if (getMethods().isEmpty()) {
            return this;
        }
        String expectedMethod = request.getHeader("Access-Control-Request-Method");
        return matchRequestMethod(expectedMethod);
    }

    @Nullable
    private RequestMethodsRequestCondition matchRequestMethod(String httpMethodValue) {
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(httpMethodValue);
            if (getMethods().contains(requestMethod)) {
                return requestMethodConditionCache.get(httpMethodValue);
            }
            if (requestMethod.equals(RequestMethod.HEAD) && getMethods().contains(RequestMethod.GET)) {
                return requestMethodConditionCache.get(HttpMethod.GET.name());
            }
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public int compareTo(RequestMethodsRequestCondition other, HttpServletRequest request) {
        if (other.methods.size() != this.methods.size()) {
            return other.methods.size() - this.methods.size();
        }
        if (this.methods.size() == 1) {
            if (this.methods.contains(RequestMethod.HEAD) && other.methods.contains(RequestMethod.GET)) {
                return -1;
            }
            if (this.methods.contains(RequestMethod.GET) && other.methods.contains(RequestMethod.HEAD)) {
                return 1;
            }
            return 0;
        }
        return 0;
    }
}
