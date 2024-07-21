package org.apache.catalina.filters;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/filters/CsrfPreventionFilter.class */
public class CsrfPreventionFilter extends CsrfPreventionFilterBase {
    private final Log log = LogFactory.getLog(CsrfPreventionFilter.class);
    private final Set<String> entryPoints = new HashSet();
    private int nonceCacheSize = 5;
    private String nonceRequestParameterName = "org.apache.catalina.filters.CSRF_NONCE";

    public void setEntryPoints(String entryPoints) {
        String[] values = entryPoints.split(",");
        for (String value : values) {
            this.entryPoints.add(value.trim());
        }
    }

    public void setNonceCacheSize(int nonceCacheSize) {
        this.nonceCacheSize = nonceCacheSize;
    }

    public void setNonceRequestParameterName(String parameterName) {
        this.nonceRequestParameterName = parameterName;
    }

    @Override // org.apache.catalina.filters.CsrfPreventionFilterBase, org.apache.catalina.filters.FilterBase, javax.servlet.Filter
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        filterConfig.getServletContext().setAttribute(Constants.CSRF_NONCE_REQUEST_PARAM_NAME_KEY, this.nonceRequestParameterName);
    }

    @Override // javax.servlet.Filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletResponse wResponse;
        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            boolean skipNonceCheck = false;
            if ("GET".equals(req.getMethod()) && this.entryPoints.contains(getRequestedPath(req))) {
                if (this.log.isTraceEnabled()) {
                    this.log.trace("Skipping CSRF nonce-check for GET request to entry point " + getRequestedPath(req));
                }
                skipNonceCheck = true;
            }
            HttpSession session = req.getSession(false);
            LruCache<String> nonceCache = session == null ? null : (LruCache) session.getAttribute("org.apache.catalina.filters.CSRF_NONCE");
            if (!skipNonceCheck) {
                String previousNonce = req.getParameter(this.nonceRequestParameterName);
                if (previousNonce == null) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Rejecting request for " + getRequestedPath(req) + ", session " + (null == session ? "(none)" : session.getId()) + " with no CSRF nonce found in request");
                    }
                    res.sendError(getDenyStatus());
                    return;
                } else if (nonceCache == null) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Rejecting request for " + getRequestedPath(req) + ", session " + (null == session ? "(none)" : session.getId()) + " due to empty / missing nonce cache");
                    }
                    res.sendError(getDenyStatus());
                    return;
                } else if (!nonceCache.contains(previousNonce)) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Rejecting request for " + getRequestedPath(req) + ", session " + (null == session ? "(none)" : session.getId()) + " due to invalid nonce " + previousNonce);
                    }
                    res.sendError(getDenyStatus());
                    return;
                } else if (this.log.isTraceEnabled()) {
                    this.log.trace("Allowing request to " + getRequestedPath(req) + " with valid CSRF nonce " + previousNonce);
                }
            }
            if (nonceCache == null) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Creating new CSRF nonce cache with size=" + this.nonceCacheSize + " for session " + (null == session ? "(will create)" : session.getId()));
                }
                nonceCache = new LruCache<>(this.nonceCacheSize);
                if (session == null) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Creating new session to store CSRF nonce cache");
                    }
                    session = req.getSession(true);
                }
                session.setAttribute("org.apache.catalina.filters.CSRF_NONCE", nonceCache);
            }
            String newNonce = generateNonce();
            nonceCache.add(newNonce);
            request.setAttribute(Constants.CSRF_NONCE_REQUEST_ATTR_NAME, newNonce);
            wResponse = new CsrfResponseWrapper(res, this.nonceRequestParameterName, newNonce);
        } else {
            wResponse = response;
        }
        chain.doFilter(request, wResponse);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/filters/CsrfPreventionFilter$CsrfResponseWrapper.class */
    protected static class CsrfResponseWrapper extends HttpServletResponseWrapper {
        private final String nonceRequestParameterName;
        private final String nonce;

        public CsrfResponseWrapper(HttpServletResponse response, String nonceRequestParameterName, String nonce) {
            super(response);
            this.nonceRequestParameterName = nonceRequestParameterName;
            this.nonce = nonce;
        }

        @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
        @Deprecated
        public String encodeRedirectUrl(String url) {
            return encodeRedirectURL(url);
        }

        @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
        public String encodeRedirectURL(String url) {
            return addNonce(super.encodeRedirectURL(url));
        }

        @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
        @Deprecated
        public String encodeUrl(String url) {
            return encodeURL(url);
        }

        @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
        public String encodeURL(String url) {
            return addNonce(super.encodeURL(url));
        }

        private String addNonce(String url) {
            if (url == null || this.nonce == null) {
                return url;
            }
            String path = url;
            String query = "";
            String anchor = "";
            int pound = path.indexOf(35);
            if (pound >= 0) {
                anchor = path.substring(pound);
                path = path.substring(0, pound);
            }
            int question = path.indexOf(63);
            if (question >= 0) {
                query = path.substring(question);
                path = path.substring(0, question);
            }
            StringBuilder sb = new StringBuilder(path);
            if (query.length() > 0) {
                sb.append(query);
                sb.append('&');
            } else {
                sb.append('?');
            }
            sb.append(this.nonceRequestParameterName);
            sb.append('=');
            sb.append(this.nonce);
            sb.append(anchor);
            return sb.toString();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/filters/CsrfPreventionFilter$LruCache.class */
    protected static class LruCache<T> implements Serializable {
        private static final long serialVersionUID = 1;
        private final Map<T, T> cache;

        public LruCache(final int cacheSize) {
            this.cache = new LinkedHashMap<T, T>() { // from class: org.apache.catalina.filters.CsrfPreventionFilter.LruCache.1
                private static final long serialVersionUID = 1;

                @Override // java.util.LinkedHashMap
                protected boolean removeEldestEntry(Map.Entry<T, T> eldest) {
                    if (size() > cacheSize) {
                        return true;
                    }
                    return false;
                }
            };
        }

        public void add(T key) {
            synchronized (this.cache) {
                this.cache.put(key, null);
            }
        }

        public boolean contains(T key) {
            boolean containsKey;
            synchronized (this.cache) {
                containsKey = this.cache.containsKey(key);
            }
            return containsKey;
        }
    }
}
