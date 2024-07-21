package ch.qos.logback.core.net.ssl;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/net/ssl/SSLConfigurable.class */
public interface SSLConfigurable {
    String[] getDefaultProtocols();

    String[] getSupportedProtocols();

    void setEnabledProtocols(String[] strArr);

    String[] getDefaultCipherSuites();

    String[] getSupportedCipherSuites();

    void setEnabledCipherSuites(String[] strArr);

    void setNeedClientAuth(boolean z);

    void setWantClientAuth(boolean z);
}
