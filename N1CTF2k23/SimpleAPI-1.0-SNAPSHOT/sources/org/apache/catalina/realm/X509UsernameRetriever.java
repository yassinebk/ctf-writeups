package org.apache.catalina.realm;

import java.security.cert.X509Certificate;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/realm/X509UsernameRetriever.class */
public interface X509UsernameRetriever {
    String getUsername(X509Certificate x509Certificate);
}
