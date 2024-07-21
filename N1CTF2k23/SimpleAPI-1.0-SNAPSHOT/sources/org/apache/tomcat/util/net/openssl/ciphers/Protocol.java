package org.apache.tomcat.util.net.openssl.ciphers;

import org.apache.tomcat.util.net.Constants;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/openssl/ciphers/Protocol.class */
enum Protocol {
    SSLv3(Constants.SSL_PROTO_SSLv3),
    SSLv2(Constants.SSL_PROTO_SSLv2),
    TLSv1(Constants.SSL_PROTO_TLSv1),
    TLSv1_2(Constants.SSL_PROTO_TLSv1_2),
    TLSv1_3(Constants.SSL_PROTO_TLSv1_3);
    
    private final String openSSLName;

    Protocol(String openSSLName) {
        this.openSSLName = openSSLName;
    }

    String getOpenSSLName() {
        return this.openSSLName;
    }
}
