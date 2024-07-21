package org.apache.catalina.mbeans;

import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.management.remote.rmi.RMIJRMPServerImpl;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.jsse.JSSEUtil;
import org.apache.tomcat.util.res.StringManager;
import sun.rmi.registry.RegistryImpl;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/mbeans/JmxRemoteLifecycleListener.class */
public class JmxRemoteLifecycleListener extends SSLHostConfig implements LifecycleListener {
    private static final long serialVersionUID = 1;
    private static final Log log = LogFactory.getLog(JmxRemoteLifecycleListener.class);
    protected static final StringManager sm = StringManager.getManager(JmxRemoteLifecycleListener.class);
    protected String rmiBindAddress = null;
    protected int rmiRegistryPortPlatform = -1;
    protected int rmiServerPortPlatform = -1;
    protected boolean rmiRegistrySSL = true;
    protected boolean rmiServerSSL = true;
    protected boolean authenticate = true;
    protected String passwordFile = null;
    protected String loginModuleName = null;
    protected String accessFile = null;
    protected boolean useLocalPorts = false;
    protected transient JMXConnectorServer csPlatform = null;

    public String getRmiBindAddress() {
        return this.rmiBindAddress;
    }

    public void setRmiBindAddress(String theRmiBindAddress) {
        this.rmiBindAddress = theRmiBindAddress;
    }

    public int getRmiServerPortPlatform() {
        return this.rmiServerPortPlatform;
    }

    public void setRmiServerPortPlatform(int theRmiServerPortPlatform) {
        this.rmiServerPortPlatform = theRmiServerPortPlatform;
    }

    public int getRmiRegistryPortPlatform() {
        return this.rmiRegistryPortPlatform;
    }

    public void setRmiRegistryPortPlatform(int theRmiRegistryPortPlatform) {
        this.rmiRegistryPortPlatform = theRmiRegistryPortPlatform;
    }

    public boolean getUseLocalPorts() {
        return this.useLocalPorts;
    }

    public void setUseLocalPorts(boolean useLocalPorts) {
        this.useLocalPorts = useLocalPorts;
    }

    public boolean isRmiRegistrySSL() {
        return this.rmiRegistrySSL;
    }

    public void setRmiRegistrySSL(boolean rmiRegistrySSL) {
        this.rmiRegistrySSL = rmiRegistrySSL;
    }

    public boolean isRmiServerSSL() {
        return this.rmiServerSSL;
    }

    public void setRmiServerSSL(boolean rmiServerSSL) {
        this.rmiServerSSL = rmiServerSSL;
    }

    public boolean isAuthenticate() {
        return this.authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

    public String getPasswordFile() {
        return this.passwordFile;
    }

    public void setPasswordFile(String passwordFile) {
        this.passwordFile = passwordFile;
    }

    public String getLoginModuleName() {
        return this.loginModuleName;
    }

    public void setLoginModuleName(String loginModuleName) {
        this.loginModuleName = loginModuleName;
    }

    public String getAccessFile() {
        return this.accessFile;
    }

    public void setAccessFile(String accessFile) {
        this.accessFile = accessFile;
    }

    protected void init() {
        String rmiRegistrySSLValue = System.getProperty("com.sun.management.jmxremote.registry.ssl");
        if (rmiRegistrySSLValue != null) {
            setRmiRegistrySSL(Boolean.parseBoolean(rmiRegistrySSLValue));
        }
        String rmiServerSSLValue = System.getProperty("com.sun.management.jmxremote.ssl");
        if (rmiServerSSLValue != null) {
            setRmiServerSSL(Boolean.parseBoolean(rmiServerSSLValue));
        }
        String protocolsValue = System.getProperty("com.sun.management.jmxremote.ssl.enabled.protocols");
        if (protocolsValue != null) {
            setEnabledProtocols(protocolsValue.split(","));
        }
        String ciphersValue = System.getProperty("com.sun.management.jmxremote.ssl.enabled.cipher.suites");
        if (ciphersValue != null) {
            setCiphers(ciphersValue);
        }
        String clientAuthValue = System.getProperty("com.sun.management.jmxremote.ssl.need.client.auth");
        if (clientAuthValue != null) {
            setCertificateVerification(clientAuthValue);
        }
        String authenticateValue = System.getProperty("com.sun.management.jmxremote.authenticate");
        if (authenticateValue != null) {
            setAuthenticate(Boolean.parseBoolean(authenticateValue));
        }
        String passwordFileValue = System.getProperty("com.sun.management.jmxremote.password.file");
        if (passwordFileValue != null) {
            setPasswordFile(passwordFileValue);
        }
        String accessFileValue = System.getProperty("com.sun.management.jmxremote.access.file");
        if (accessFileValue != null) {
            setAccessFile(accessFileValue);
        }
        String loginModuleNameValue = System.getProperty("com.sun.management.jmxremote.login.config");
        if (loginModuleNameValue != null) {
            setLoginModuleName(loginModuleNameValue);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.catalina.LifecycleListener
    public void lifecycleEvent(LifecycleEvent event) {
        if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType())) {
            log.warn(sm.getString("jmxRemoteLifecycleListener.deprecated"));
        } else if (!Lifecycle.START_EVENT.equals(event.getType())) {
            if (Lifecycle.STOP_EVENT.equals(event.getType())) {
                destroyServer("Platform", this.csPlatform);
            }
        } else {
            init();
            SSLContext sslContext = null;
            if (getCertificates().size() > 0) {
                SSLHostConfigCertificate certificate = getCertificates().iterator().next();
                JSSEUtil sslUtil = new JSSEUtil(certificate);
                try {
                    sslContext = SSLContext.getInstance(getSslProtocol());
                    setEnabledProtocols(sslUtil.getEnabledProtocols());
                    setEnabledCiphers(sslUtil.getEnabledCiphers());
                    sslContext.init(sslUtil.getKeyManagers(), sslUtil.getTrustManagers(), null);
                    SSLSessionContext sessionContext = sslContext.getServerSessionContext();
                    if (sessionContext != null) {
                        sslUtil.configureSessionContext(sessionContext);
                    }
                } catch (Exception e) {
                    log.error(sm.getString("jmxRemoteLifecycleListener.invalidSSLConfiguration"), e);
                }
            }
            System.setProperty("java.rmi.server.randomIDs", "true");
            Map<String, Object> env = new HashMap<>();
            RMIClientSocketFactory registryCsf = null;
            RMIServerSocketFactory registrySsf = null;
            RMIClientSocketFactory serverCsf = null;
            RMIServerSocketFactory serverSsf = null;
            if (this.rmiRegistrySSL) {
                registryCsf = new SslRMIClientSocketFactory();
                if (this.rmiBindAddress == null) {
                    registrySsf = new SslRMIServerSocketFactory(sslContext, getEnabledCiphers(), getEnabledProtocols(), getCertificateVerification() == SSLHostConfig.CertificateVerification.REQUIRED);
                } else {
                    registrySsf = new SslRmiServerBindSocketFactory(sslContext, getEnabledCiphers(), getEnabledProtocols(), getCertificateVerification() == SSLHostConfig.CertificateVerification.REQUIRED, this.rmiBindAddress);
                }
            } else if (this.rmiBindAddress != null) {
                registrySsf = new RmiServerBindSocketFactory(this.rmiBindAddress);
            }
            if (this.rmiServerSSL) {
                serverCsf = new SslRMIClientSocketFactory();
                if (this.rmiBindAddress == null) {
                    serverSsf = new SslRMIServerSocketFactory(sslContext, getEnabledCiphers(), getEnabledProtocols(), getCertificateVerification() == SSLHostConfig.CertificateVerification.REQUIRED);
                } else {
                    serverSsf = new SslRmiServerBindSocketFactory(sslContext, getEnabledCiphers(), getEnabledProtocols(), getCertificateVerification() == SSLHostConfig.CertificateVerification.REQUIRED, this.rmiBindAddress);
                }
            } else if (this.rmiBindAddress != null) {
                serverSsf = new RmiServerBindSocketFactory(this.rmiBindAddress);
            }
            if (this.rmiBindAddress != null) {
                System.setProperty("java.rmi.server.hostname", this.rmiBindAddress);
            }
            if (this.useLocalPorts) {
                registryCsf = new RmiClientLocalhostSocketFactory(registryCsf);
                serverCsf = new RmiClientLocalhostSocketFactory(serverCsf);
            }
            env.put("jmx.remote.rmi.server.credential.types", new String[]{String[].class.getName(), String.class.getName()});
            if (serverCsf != null) {
                env.put("jmx.remote.rmi.client.socket.factory", serverCsf);
                env.put("com.sun.jndi.rmi.factory.socket", registryCsf);
            }
            if (serverSsf != null) {
                env.put("jmx.remote.rmi.server.socket.factory", serverSsf);
            }
            if (this.authenticate) {
                env.put("jmx.remote.x.password.file", this.passwordFile);
                env.put("jmx.remote.x.access.file", this.accessFile);
                env.put("jmx.remote.x.login.config", this.loginModuleName);
            }
            this.csPlatform = createServer("Platform", this.rmiBindAddress, this.rmiRegistryPortPlatform, this.rmiServerPortPlatform, env, registryCsf, registrySsf, serverCsf, serverSsf);
        }
    }

    private JMXConnectorServer createServer(String serverName, String bindAddress, int theRmiRegistryPort, int theRmiServerPort, Map<String, Object> theEnv, RMIClientSocketFactory registryCsf, RMIServerSocketFactory registrySsf, RMIClientSocketFactory serverCsf, RMIServerSocketFactory serverSsf) {
        if (bindAddress == null) {
            bindAddress = "localhost";
        }
        String url = "service:jmx:rmi://" + bindAddress;
        try {
            JMXServiceURL serviceUrl = new JMXServiceURL(url);
            RMIConnectorServer cs = null;
            try {
                RMIJRMPServerImpl server = new RMIJRMPServerImpl(this.rmiServerPortPlatform, serverCsf, serverSsf, theEnv);
                cs = new RMIConnectorServer(serviceUrl, theEnv, server, ManagementFactory.getPlatformMBeanServer());
                cs.start();
                Remote jmxServer = server.toStub();
                try {
                    new JmxRegistry(theRmiRegistryPort, registryCsf, registrySsf, "jmxrmi", jmxServer);
                    log.info(sm.getString("jmxRemoteLifecycleListener.start", Integer.toString(theRmiRegistryPort), Integer.toString(theRmiServerPort), serverName));
                } catch (RemoteException e) {
                    log.error(sm.getString("jmxRemoteLifecycleListener.createRegistryFailed", serverName, Integer.toString(theRmiRegistryPort)), e);
                    return null;
                }
            } catch (IOException e2) {
                log.error(sm.getString("jmxRemoteLifecycleListener.createServerFailed", serverName), e2);
            }
            return cs;
        } catch (MalformedURLException e3) {
            log.error(sm.getString("jmxRemoteLifecycleListener.invalidURL", serverName, url), e3);
            return null;
        }
    }

    private void destroyServer(String serverName, JMXConnectorServer theConnectorServer) {
        if (theConnectorServer != null) {
            try {
                theConnectorServer.stop();
            } catch (IOException e) {
                log.error(sm.getString("jmxRemoteLifecycleListener.destroyServerFailed", serverName), e);
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/mbeans/JmxRemoteLifecycleListener$RmiClientLocalhostSocketFactory.class */
    public static class RmiClientLocalhostSocketFactory implements RMIClientSocketFactory, Serializable {
        private static final long serialVersionUID = 1;
        private static final String FORCED_HOST = "localhost";
        private final RMIClientSocketFactory factory;

        public RmiClientLocalhostSocketFactory(RMIClientSocketFactory theFactory) {
            this.factory = theFactory;
        }

        public Socket createSocket(String host, int port) throws IOException {
            if (this.factory == null) {
                return new Socket(FORCED_HOST, port);
            }
            return this.factory.createSocket(FORCED_HOST, port);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/mbeans/JmxRemoteLifecycleListener$RmiServerBindSocketFactory.class */
    public static class RmiServerBindSocketFactory implements RMIServerSocketFactory {
        private final InetAddress bindAddress;

        public RmiServerBindSocketFactory(String address) {
            InetAddress bindAddress = null;
            try {
                bindAddress = InetAddress.getByName(address);
            } catch (UnknownHostException e) {
                JmxRemoteLifecycleListener.log.error(JmxRemoteLifecycleListener.sm.getString("jmxRemoteLifecycleListener.invalidRmiBindAddress", address), e);
            }
            this.bindAddress = bindAddress;
        }

        public ServerSocket createServerSocket(int port) throws IOException {
            return new ServerSocket(port, 0, this.bindAddress);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/mbeans/JmxRemoteLifecycleListener$SslRmiServerBindSocketFactory.class */
    public static class SslRmiServerBindSocketFactory extends SslRMIServerSocketFactory {
        private final InetAddress bindAddress;
        private final SSLContext sslContext;

        public SslRmiServerBindSocketFactory(SSLContext sslContext, String[] enabledCipherSuites, String[] enabledProtocols, boolean needClientAuth, String address) {
            super(sslContext, enabledCipherSuites, enabledProtocols, needClientAuth);
            this.sslContext = sslContext;
            InetAddress bindAddress = null;
            try {
                bindAddress = InetAddress.getByName(address);
            } catch (UnknownHostException e) {
                JmxRemoteLifecycleListener.log.error(JmxRemoteLifecycleListener.sm.getString("jmxRemoteLifecycleListener.invalidRmiBindAddress", address), e);
            }
            this.bindAddress = bindAddress;
        }

        public ServerSocket createServerSocket(int port) throws IOException {
            SSLServerSocketFactory serverSocketFactory;
            if (this.sslContext == null) {
                serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            } else {
                serverSocketFactory = this.sslContext.getServerSocketFactory();
            }
            SSLServerSocketFactory sslServerSocketFactory = serverSocketFactory;
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port, 0, this.bindAddress);
            if (getEnabledCipherSuites() != null) {
                sslServerSocket.setEnabledCipherSuites(getEnabledCipherSuites());
            }
            if (getEnabledProtocols() != null) {
                sslServerSocket.setEnabledProtocols(getEnabledProtocols());
            }
            sslServerSocket.setNeedClientAuth(getNeedClientAuth());
            return sslServerSocket;
        }

        public int hashCode() {
            int result = super.hashCode();
            return (31 * result) + (this.bindAddress == null ? 0 : this.bindAddress.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            SslRmiServerBindSocketFactory other = (SslRmiServerBindSocketFactory) obj;
            if (this.bindAddress == null) {
                if (other.bindAddress != null) {
                    return false;
                }
                return true;
            } else if (!this.bindAddress.equals(other.bindAddress)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/mbeans/JmxRemoteLifecycleListener$JmxRegistry.class */
    public static class JmxRegistry extends RegistryImpl {
        private static final long serialVersionUID = -3772054804656428217L;
        private final String jmxName;
        private final Remote jmxServer;

        public JmxRegistry(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf, String jmxName, Remote jmxServer) throws RemoteException {
            super(port, csf, ssf);
            this.jmxName = jmxName;
            this.jmxServer = jmxServer;
        }

        public Remote lookup(String name) throws RemoteException, NotBoundException {
            if (this.jmxName.equals(name)) {
                return this.jmxServer;
            }
            return null;
        }

        public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
        }

        public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
        }

        public void rebind(String name, Remote obj) throws RemoteException, AccessException {
        }

        public String[] list() throws RemoteException {
            return new String[]{this.jmxName};
        }
    }
}
