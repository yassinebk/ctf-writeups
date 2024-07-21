package org.apache.coyote;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.coyote.ajp.AjpAprProtocol;
import org.apache.coyote.ajp.AjpNioProtocol;
import org.apache.coyote.http11.Http11AprProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.net.SSLHostConfig;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/ProtocolHandler.class */
public interface ProtocolHandler {
    Adapter getAdapter();

    void setAdapter(Adapter adapter);

    Executor getExecutor();

    void setExecutor(Executor executor);

    ScheduledExecutorService getUtilityExecutor();

    void setUtilityExecutor(ScheduledExecutorService scheduledExecutorService);

    void init() throws Exception;

    void start() throws Exception;

    void pause() throws Exception;

    void resume() throws Exception;

    void stop() throws Exception;

    void destroy() throws Exception;

    void closeServerSocketGraceful();

    boolean isAprRequired();

    boolean isSendfileSupported();

    void addSslHostConfig(SSLHostConfig sSLHostConfig);

    SSLHostConfig[] findSslHostConfigs();

    void addUpgradeProtocol(UpgradeProtocol upgradeProtocol);

    UpgradeProtocol[] findUpgradeProtocols();

    default int getDesiredBufferSize() {
        return -1;
    }

    static ProtocolHandler create(String protocol, boolean apr) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (protocol == null || org.apache.coyote.http11.Constants.HTTP_11.equals(protocol) || ((!apr && Http11NioProtocol.class.getName().equals(protocol)) || (apr && Http11AprProtocol.class.getName().equals(protocol)))) {
            if (apr) {
                return new Http11AprProtocol();
            }
            return new Http11NioProtocol();
        } else if ("AJP/1.3".equals(protocol) || ((!apr && AjpNioProtocol.class.getName().equals(protocol)) || (apr && AjpAprProtocol.class.getName().equals(protocol)))) {
            if (apr) {
                return new AjpAprProtocol();
            }
            return new AjpNioProtocol();
        } else {
            Class<?> clazz = Class.forName(protocol);
            return (ProtocolHandler) clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
    }
}
