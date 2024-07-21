package org.apache.catalina.util;

import org.apache.tomcat.util.net.SSLSupport;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/util/TLSUtil.class */
public class TLSUtil {
    public static boolean isTLSRequestAttribute(String name) {
        return "javax.servlet.request.X509Certificate".equals(name) || "javax.servlet.request.cipher_suite".equals(name) || "javax.servlet.request.key_size".equals(name) || "javax.servlet.request.ssl_session_id".equals(name) || "javax.servlet.request.ssl_session_mgr".equals(name) || SSLSupport.PROTOCOL_VERSION_KEY.equals(name);
    }
}
