package org.springframework.web.servlet;

import javax.servlet.ServletException;
import org.springframework.http.HttpHeaders;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/NoHandlerFoundException.class */
public class NoHandlerFoundException extends ServletException {
    private final String httpMethod;
    private final String requestURL;
    private final HttpHeaders headers;

    public NoHandlerFoundException(String httpMethod, String requestURL, HttpHeaders headers) {
        super("No handler found for " + httpMethod + " " + requestURL);
        this.httpMethod = httpMethod;
        this.requestURL = requestURL;
        this.headers = headers;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public String getRequestURL() {
        return this.requestURL;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }
}
