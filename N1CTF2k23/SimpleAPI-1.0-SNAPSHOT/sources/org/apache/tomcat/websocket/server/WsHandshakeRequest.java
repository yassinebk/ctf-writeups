package org.apache.tomcat.websocket.server;

import ch.qos.logback.classic.spi.CallerData;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;
import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/server/WsHandshakeRequest.class */
public class WsHandshakeRequest implements HandshakeRequest {
    private static final StringManager sm = StringManager.getManager(WsHandshakeRequest.class);
    private final URI requestUri;
    private final Map<String, List<String>> parameterMap;
    private final String queryString;
    private final Principal userPrincipal;
    private final Map<String, List<String>> headers;
    private final Object httpSession;
    private volatile HttpServletRequest request;

    public WsHandshakeRequest(HttpServletRequest request, Map<String, String> pathParams) {
        this.request = request;
        this.queryString = request.getQueryString();
        this.userPrincipal = request.getUserPrincipal();
        this.httpSession = request.getSession(false);
        this.requestUri = buildRequestUri(request);
        Map<String, String[]> originalParameters = request.getParameterMap();
        Map<String, List<String>> newParameters = new HashMap<>(originalParameters.size());
        for (Map.Entry<String, String[]> entry : originalParameters.entrySet()) {
            newParameters.put(entry.getKey(), Collections.unmodifiableList(Arrays.asList(entry.getValue())));
        }
        for (Map.Entry<String, String> entry2 : pathParams.entrySet()) {
            newParameters.put(entry2.getKey(), Collections.singletonList(entry2.getValue()));
        }
        this.parameterMap = Collections.unmodifiableMap(newParameters);
        Map<String, List<String>> newHeaders = new CaseInsensitiveKeyMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            newHeaders.put(headerName, Collections.unmodifiableList(Collections.list(request.getHeaders(headerName))));
        }
        this.headers = Collections.unmodifiableMap(newHeaders);
    }

    @Override // javax.websocket.server.HandshakeRequest
    public URI getRequestURI() {
        return this.requestUri;
    }

    @Override // javax.websocket.server.HandshakeRequest
    public Map<String, List<String>> getParameterMap() {
        return this.parameterMap;
    }

    @Override // javax.websocket.server.HandshakeRequest
    public String getQueryString() {
        return this.queryString;
    }

    @Override // javax.websocket.server.HandshakeRequest
    public Principal getUserPrincipal() {
        return this.userPrincipal;
    }

    @Override // javax.websocket.server.HandshakeRequest
    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    @Override // javax.websocket.server.HandshakeRequest
    public boolean isUserInRole(String role) {
        if (this.request == null) {
            throw new IllegalStateException();
        }
        return this.request.isUserInRole(role);
    }

    @Override // javax.websocket.server.HandshakeRequest
    public Object getHttpSession() {
        return this.httpSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finished() {
        this.request = null;
    }

    private static URI buildRequestUri(HttpServletRequest req) {
        StringBuffer uri = new StringBuffer();
        String scheme = req.getScheme();
        int port = req.getServerPort();
        if (port < 0) {
            port = 80;
        }
        if ("http".equals(scheme)) {
            uri.append("ws");
        } else if ("https".equals(scheme)) {
            uri.append("wss");
        } else {
            throw new IllegalArgumentException(sm.getString("wsHandshakeRequest.unknownScheme", scheme));
        }
        uri.append("://");
        uri.append(req.getServerName());
        if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
            uri.append(':');
            uri.append(port);
        }
        uri.append(req.getRequestURI());
        if (req.getQueryString() != null) {
            uri.append(CallerData.NA);
            uri.append(req.getQueryString());
        }
        try {
            return new URI(uri.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(sm.getString("wsHandshakeRequest.invalidUri", uri.toString()), e);
        }
    }
}
