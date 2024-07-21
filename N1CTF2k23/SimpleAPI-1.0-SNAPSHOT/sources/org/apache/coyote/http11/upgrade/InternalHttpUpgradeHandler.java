package org.apache.coyote.http11.upgrade;

import javax.servlet.http.HttpUpgradeHandler;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http11/upgrade/InternalHttpUpgradeHandler.class */
public interface InternalHttpUpgradeHandler extends HttpUpgradeHandler {
    AbstractEndpoint.Handler.SocketState upgradeDispatch(SocketEvent socketEvent);

    void timeoutAsync(long j);

    void setSocketWrapper(SocketWrapperBase<?> socketWrapperBase);

    void setSslSupport(SSLSupport sSLSupport);

    void pause();

    default boolean hasAsyncIO() {
        return false;
    }
}
