package org.apache.coyote.http11;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.NioChannel;
import org.apache.tomcat.util.net.NioEndpoint;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/Http11NioProtocol.class */
public class Http11NioProtocol extends AbstractHttp11JsseProtocol<NioChannel> {
    private static final Log log = LogFactory.getLog(Http11NioProtocol.class);

    public Http11NioProtocol() {
        super(new NioEndpoint());
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected Log getLog() {
        return log;
    }

    @Deprecated
    public void setPollerThreadCount(int count) {
    }

    @Deprecated
    public int getPollerThreadCount() {
        return 1;
    }

    public void setSelectorTimeout(long timeout) {
        ((NioEndpoint) getEndpoint()).setSelectorTimeout(timeout);
    }

    public long getSelectorTimeout() {
        return ((NioEndpoint) getEndpoint()).getSelectorTimeout();
    }

    public void setPollerThreadPriority(int threadPriority) {
        ((NioEndpoint) getEndpoint()).setPollerThreadPriority(threadPriority);
    }

    public int getPollerThreadPriority() {
        return ((NioEndpoint) getEndpoint()).getPollerThreadPriority();
    }

    @Override // org.apache.coyote.AbstractProtocol
    protected String getNamePrefix() {
        if (isSSLEnabled()) {
            return "https-" + getSslImplementationShortName() + "-nio";
        }
        return "http-nio";
    }
}
