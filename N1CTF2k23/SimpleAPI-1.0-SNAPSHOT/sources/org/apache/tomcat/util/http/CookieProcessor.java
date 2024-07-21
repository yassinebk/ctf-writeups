package org.apache.tomcat.util.http;

import java.nio.charset.Charset;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/CookieProcessor.class */
public interface CookieProcessor {
    void parseCookieHeader(MimeHeaders mimeHeaders, ServerCookies serverCookies);

    @Deprecated
    String generateHeader(Cookie cookie);

    Charset getCharset();

    default String generateHeader(Cookie cookie, HttpServletRequest request) {
        return generateHeader(cookie);
    }
}
