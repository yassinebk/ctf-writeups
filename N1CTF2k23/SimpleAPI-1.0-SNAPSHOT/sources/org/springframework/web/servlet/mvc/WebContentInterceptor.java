package org.springframework.web.servlet.mvc;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.util.UrlPathHelper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/WebContentInterceptor.class */
public class WebContentInterceptor extends WebContentGenerator implements HandlerInterceptor {
    private UrlPathHelper urlPathHelper;
    private PathMatcher pathMatcher;
    private Map<String, Integer> cacheMappings;
    private Map<String, CacheControl> cacheControlMappings;

    public WebContentInterceptor() {
        super(false);
        this.urlPathHelper = new UrlPathHelper();
        this.pathMatcher = new AntPathMatcher();
        this.cacheMappings = new HashMap();
        this.cacheControlMappings = new HashMap();
    }

    public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
        this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
    }

    public void setUrlDecode(boolean urlDecode) {
        this.urlPathHelper.setUrlDecode(urlDecode);
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
        this.urlPathHelper = urlPathHelper;
    }

    public void setCacheMappings(Properties cacheMappings) {
        this.cacheMappings.clear();
        Enumeration<?> propNames = cacheMappings.propertyNames();
        while (propNames.hasMoreElements()) {
            String path = (String) propNames.nextElement();
            int cacheSeconds = Integer.parseInt(cacheMappings.getProperty(path));
            this.cacheMappings.put(path, Integer.valueOf(cacheSeconds));
        }
    }

    public void addCacheMapping(CacheControl cacheControl, String... paths) {
        for (String path : paths) {
            this.cacheControlMappings.put(path, cacheControl);
        }
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        Assert.notNull(pathMatcher, "PathMatcher must not be null");
        this.pathMatcher = pathMatcher;
    }

    @Override // org.springframework.web.servlet.HandlerInterceptor
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        checkRequest(request);
        String lookupPath = this.urlPathHelper.getLookupPathForRequest(request, HandlerMapping.LOOKUP_PATH);
        CacheControl cacheControl = lookupCacheControl(lookupPath);
        Integer cacheSeconds = lookupCacheSeconds(lookupPath);
        if (cacheControl != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Applying " + cacheControl);
            }
            applyCacheControl(response, cacheControl);
            return true;
        } else if (cacheSeconds != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Applying cacheSeconds " + cacheSeconds);
            }
            applyCacheSeconds(response, cacheSeconds.intValue());
            return true;
        } else {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Applying default cacheSeconds");
            }
            prepareResponse(response);
            return true;
        }
    }

    @Nullable
    protected CacheControl lookupCacheControl(String urlPath) {
        CacheControl cacheControl = this.cacheControlMappings.get(urlPath);
        if (cacheControl != null) {
            return cacheControl;
        }
        for (Map.Entry<String, CacheControl> entry : this.cacheControlMappings.entrySet()) {
            if (this.pathMatcher.match(entry.getKey(), urlPath)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Nullable
    protected Integer lookupCacheSeconds(String urlPath) {
        Integer cacheSeconds = this.cacheMappings.get(urlPath);
        if (cacheSeconds != null) {
            return cacheSeconds;
        }
        for (Map.Entry<String, Integer> entry : this.cacheMappings.entrySet()) {
            if (this.pathMatcher.match(entry.getKey(), urlPath)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override // org.springframework.web.servlet.HandlerInterceptor
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override // org.springframework.web.servlet.HandlerInterceptor
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
