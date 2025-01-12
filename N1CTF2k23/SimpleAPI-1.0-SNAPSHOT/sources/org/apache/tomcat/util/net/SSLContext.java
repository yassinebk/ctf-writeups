package org.apache.tomcat.util.net;

import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SSLContext.class */
public interface SSLContext {
    void init(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) throws KeyManagementException;

    void destroy();

    SSLSessionContext getServerSessionContext();

    SSLEngine createSSLEngine();

    SSLServerSocketFactory getServerSocketFactory();

    SSLParameters getSupportedSSLParameters();

    X509Certificate[] getCertificateChain(String str);

    X509Certificate[] getAcceptedIssuers();
}
