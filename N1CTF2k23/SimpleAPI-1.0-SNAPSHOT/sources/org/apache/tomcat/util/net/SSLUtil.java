package org.apache.tomcat.util.net;

import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SSLUtil.class */
public interface SSLUtil {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SSLUtil$ProtocolInfo.class */
    public interface ProtocolInfo {
        String getNegotiatedProtocol();
    }

    SSLContext createSSLContext(List<String> list) throws Exception;

    KeyManager[] getKeyManagers() throws Exception;

    TrustManager[] getTrustManagers() throws Exception;

    void configureSessionContext(SSLSessionContext sSLSessionContext);

    String[] getEnabledProtocols() throws IllegalArgumentException;

    String[] getEnabledCiphers() throws IllegalArgumentException;
}
