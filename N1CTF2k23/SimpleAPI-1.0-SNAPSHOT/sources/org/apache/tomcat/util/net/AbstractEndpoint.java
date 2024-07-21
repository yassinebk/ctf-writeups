package org.apache.tomcat.util.net;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.juli.logging.Log;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tomcat.util.collections.SynchronizedStack;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.net.Acceptor;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.threads.LimitLatch;
import org.apache.tomcat.util.threads.ResizableExecutor;
import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/AbstractEndpoint.class */
public abstract class AbstractEndpoint<S, U> {
    protected static final StringManager sm = StringManager.getManager(AbstractEndpoint.class);
    protected Acceptor<U> acceptor;
    protected SynchronizedStack<SocketProcessorBase<S>> processorCache;
    private InetAddress address;
    private String domain;
    protected volatile boolean running = false;
    protected volatile boolean paused = false;
    protected volatile boolean internalExecutor = true;
    private volatile LimitLatch connectionLimitLatch = null;
    protected final SocketProperties socketProperties = new SocketProperties();
    private ObjectName oname = null;
    protected Map<U, SocketWrapperBase<S>> connections = new ConcurrentHashMap();
    private String defaultSSLHostConfigName = "_default_";
    protected ConcurrentMap<String, SSLHostConfig> sslHostConfigs = new ConcurrentHashMap();
    private boolean useSendfile = true;
    private long executorTerminationTimeoutMillis = 5000;
    protected int acceptorThreadCount = 1;
    protected int acceptorThreadPriority = 5;
    private int maxConnections = 8192;
    private Executor executor = null;
    private ScheduledExecutorService utilityExecutor = null;
    private int port = -1;
    private int portOffset = 0;
    private int acceptCount = 100;
    private boolean bindOnInit = true;
    private volatile BindState bindState = BindState.UNBOUND;
    private Integer keepAliveTimeout = null;
    private boolean SSLEnabled = false;
    private int minSpareThreads = 10;
    private int maxThreads = 200;
    protected int threadPriority = 5;
    private int maxKeepAliveRequests = 100;
    private String name = "TP";
    private boolean daemon = true;
    private boolean useAsyncIO = true;
    protected final List<String> negotiableProtocols = new ArrayList();
    private Handler<S> handler = null;
    protected HashMap<String, Object> attributes = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/AbstractEndpoint$BindState.class */
    public enum BindState {
        UNBOUND,
        BOUND_ON_INIT,
        BOUND_ON_START,
        SOCKET_CLOSED_ON_STOP
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/AbstractEndpoint$Handler.class */
    public interface Handler<S> {

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/AbstractEndpoint$Handler$SocketState.class */
        public enum SocketState {
            OPEN,
            CLOSED,
            LONG,
            ASYNC_END,
            SENDFILE,
            UPGRADING,
            UPGRADED,
            SUSPENDED
        }

        SocketState process(SocketWrapperBase<S> socketWrapperBase, SocketEvent socketEvent);

        Object getGlobal();

        @Deprecated
        Set<S> getOpenSockets();

        void release(SocketWrapperBase<S> socketWrapperBase);

        void pause();

        void recycle();
    }

    protected abstract void createSSLContext(SSLHostConfig sSLHostConfig) throws Exception;

    protected abstract InetSocketAddress getLocalAddress() throws IOException;

    public abstract boolean isAlpnSupported();

    protected abstract boolean getDeferAccept();

    protected abstract SocketProcessorBase<S> createSocketProcessor(SocketWrapperBase<S> socketWrapperBase, SocketEvent socketEvent);

    public abstract void bind() throws Exception;

    public abstract void unbind() throws Exception;

    public abstract void startInternal() throws Exception;

    public abstract void stopInternal() throws Exception;

    protected abstract Log getLog();

    protected abstract void doCloseServerSocket() throws IOException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract U serverSocketAccept() throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean setSocketOptions(U u);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void destroySocket(U u);

    public static long toTimeout(long timeout) {
        if (timeout > 0) {
            return timeout;
        }
        return Long.MAX_VALUE;
    }

    public SocketProperties getSocketProperties() {
        return this.socketProperties;
    }

    public Set<SocketWrapperBase<S>> getConnections() {
        return new HashSet(this.connections.values());
    }

    public String getDefaultSSLHostConfigName() {
        return this.defaultSSLHostConfigName;
    }

    public void setDefaultSSLHostConfigName(String defaultSSLHostConfigName) {
        this.defaultSSLHostConfigName = defaultSSLHostConfigName;
    }

    public void addSslHostConfig(SSLHostConfig sslHostConfig) throws IllegalArgumentException {
        addSslHostConfig(sslHostConfig, false);
    }

    public void addSslHostConfig(SSLHostConfig sslHostConfig, boolean replace) throws IllegalArgumentException {
        String key = sslHostConfig.getHostName();
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException(sm.getString("endpoint.noSslHostName"));
        }
        if (this.bindState != BindState.UNBOUND && this.bindState != BindState.SOCKET_CLOSED_ON_STOP && isSSLEnabled()) {
            try {
                createSSLContext(sslHostConfig);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        if (replace) {
            SSLHostConfig previous = this.sslHostConfigs.put(key, sslHostConfig);
            if (previous != null) {
                unregisterJmx(sslHostConfig);
            }
            registerJmx(sslHostConfig);
            return;
        }
        SSLHostConfig duplicate = this.sslHostConfigs.putIfAbsent(key, sslHostConfig);
        if (duplicate != null) {
            releaseSSLContext(sslHostConfig);
            throw new IllegalArgumentException(sm.getString("endpoint.duplicateSslHostName", key));
        } else {
            registerJmx(sslHostConfig);
        }
    }

    public SSLHostConfig removeSslHostConfig(String hostName) {
        if (hostName == null) {
            return null;
        }
        if (hostName.equalsIgnoreCase(getDefaultSSLHostConfigName())) {
            throw new IllegalArgumentException(sm.getString("endpoint.removeDefaultSslHostConfig", hostName));
        }
        SSLHostConfig sslHostConfig = this.sslHostConfigs.remove(hostName);
        unregisterJmx(sslHostConfig);
        return sslHostConfig;
    }

    public void reloadSslHostConfig(String hostName) {
        SSLHostConfig sslHostConfig = this.sslHostConfigs.get(hostName);
        if (sslHostConfig == null) {
            throw new IllegalArgumentException(sm.getString("endpoint.unknownSslHostName", hostName));
        }
        addSslHostConfig(sslHostConfig, true);
    }

    public void reloadSslHostConfigs() {
        for (String hostName : this.sslHostConfigs.keySet()) {
            reloadSslHostConfig(hostName);
        }
    }

    public SSLHostConfig[] findSslHostConfigs() {
        return (SSLHostConfig[]) this.sslHostConfigs.values().toArray(new SSLHostConfig[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void destroySsl() throws Exception {
        if (isSSLEnabled()) {
            for (SSLHostConfig sslHostConfig : this.sslHostConfigs.values()) {
                releaseSSLContext(sslHostConfig);
            }
        }
    }

    protected void releaseSSLContext(SSLHostConfig sslHostConfig) {
        SSLContext sslContext;
        for (SSLHostConfigCertificate certificate : sslHostConfig.getCertificates(true)) {
            if (certificate.getSslContext() != null && (sslContext = certificate.getSslContext()) != null) {
                sslContext.destroy();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SSLHostConfig getSSLHostConfig(String sniHostName) {
        SSLHostConfig result = null;
        if (sniHostName != null) {
            result = this.sslHostConfigs.get(sniHostName);
            if (result != null) {
                return result;
            }
            int indexOfDot = sniHostName.indexOf(46);
            if (indexOfDot > -1) {
                result = this.sslHostConfigs.get("*" + sniHostName.substring(indexOfDot));
            }
        }
        if (result == null) {
            result = this.sslHostConfigs.get(getDefaultSSLHostConfigName());
        }
        if (result == null) {
            throw new IllegalStateException();
        }
        return result;
    }

    public boolean getUseSendfile() {
        return this.useSendfile;
    }

    public void setUseSendfile(boolean useSendfile) {
        this.useSendfile = useSendfile;
    }

    public long getExecutorTerminationTimeoutMillis() {
        return this.executorTerminationTimeoutMillis;
    }

    public void setExecutorTerminationTimeoutMillis(long executorTerminationTimeoutMillis) {
        this.executorTerminationTimeoutMillis = executorTerminationTimeoutMillis;
    }

    @Deprecated
    public void setAcceptorThreadCount(int acceptorThreadCount) {
    }

    @Deprecated
    public int getAcceptorThreadCount() {
        return 1;
    }

    public void setAcceptorThreadPriority(int acceptorThreadPriority) {
        this.acceptorThreadPriority = acceptorThreadPriority;
    }

    public int getAcceptorThreadPriority() {
        return this.acceptorThreadPriority;
    }

    public void setMaxConnections(int maxCon) {
        this.maxConnections = maxCon;
        LimitLatch latch = this.connectionLimitLatch;
        if (latch != null) {
            if (maxCon == -1) {
                releaseConnectionLatch();
            } else {
                latch.setLimit(maxCon);
            }
        } else if (maxCon > 0) {
            initializeConnectionLatch();
        }
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public long getConnectionCount() {
        LimitLatch latch = this.connectionLimitLatch;
        if (latch != null) {
            return latch.getCount();
        }
        return -1L;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
        this.internalExecutor = executor == null;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public void setUtilityExecutor(ScheduledExecutorService utilityExecutor) {
        this.utilityExecutor = utilityExecutor;
    }

    public ScheduledExecutorService getUtilityExecutor() {
        if (this.utilityExecutor == null) {
            getLog().warn(sm.getString("endpoint.warn.noUtilityExecutor"));
            this.utilityExecutor = new ScheduledThreadPoolExecutor(1);
        }
        return this.utilityExecutor;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPortOffset() {
        return this.portOffset;
    }

    public void setPortOffset(int portOffset) {
        if (portOffset < 0) {
            throw new IllegalArgumentException(sm.getString("endpoint.portOffset.invalid", Integer.valueOf(portOffset)));
        }
        this.portOffset = portOffset;
    }

    public int getPortWithOffset() {
        int port = getPort();
        if (port > 0) {
            return port + getPortOffset();
        }
        return port;
    }

    public final int getLocalPort() {
        try {
            InetSocketAddress localAddress = getLocalAddress();
            if (localAddress == null) {
                return -1;
            }
            return localAddress.getPort();
        } catch (IOException e) {
            return -1;
        }
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public void setAcceptCount(int acceptCount) {
        if (acceptCount > 0) {
            this.acceptCount = acceptCount;
        }
    }

    public int getAcceptCount() {
        return this.acceptCount;
    }

    public boolean getBindOnInit() {
        return this.bindOnInit;
    }

    public void setBindOnInit(boolean b) {
        this.bindOnInit = b;
    }

    public int getKeepAliveTimeout() {
        if (this.keepAliveTimeout == null) {
            return getConnectionTimeout();
        }
        return this.keepAliveTimeout.intValue();
    }

    public void setKeepAliveTimeout(int keepAliveTimeout) {
        this.keepAliveTimeout = Integer.valueOf(keepAliveTimeout);
    }

    public boolean getTcpNoDelay() {
        return this.socketProperties.getTcpNoDelay();
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.socketProperties.setTcpNoDelay(tcpNoDelay);
    }

    public int getConnectionLinger() {
        return this.socketProperties.getSoLingerTime();
    }

    public void setConnectionLinger(int connectionLinger) {
        this.socketProperties.setSoLingerTime(connectionLinger);
        this.socketProperties.setSoLingerOn(connectionLinger >= 0);
    }

    public int getConnectionTimeout() {
        return this.socketProperties.getSoTimeout();
    }

    public void setConnectionTimeout(int soTimeout) {
        this.socketProperties.setSoTimeout(soTimeout);
    }

    public boolean isSSLEnabled() {
        return this.SSLEnabled;
    }

    public void setSSLEnabled(boolean SSLEnabled) {
        this.SSLEnabled = SSLEnabled;
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
        Executor executor = this.executor;
        if (this.internalExecutor && (executor instanceof ThreadPoolExecutor)) {
            ((ThreadPoolExecutor) executor).setCorePoolSize(minSpareThreads);
        }
    }

    public int getMinSpareThreads() {
        return Math.min(getMinSpareThreadsInternal(), getMaxThreads());
    }

    private int getMinSpareThreadsInternal() {
        if (this.internalExecutor) {
            return this.minSpareThreads;
        }
        return -1;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        Executor executor = this.executor;
        if (this.internalExecutor && (executor instanceof ThreadPoolExecutor)) {
            ((ThreadPoolExecutor) executor).setMaximumPoolSize(maxThreads);
        }
    }

    public int getMaxThreads() {
        if (this.internalExecutor) {
            return this.maxThreads;
        }
        return -1;
    }

    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public int getThreadPriority() {
        if (this.internalExecutor) {
            return this.threadPriority;
        }
        return -1;
    }

    public int getMaxKeepAliveRequests() {
        return this.maxKeepAliveRequests;
    }

    public void setMaxKeepAliveRequests(int maxKeepAliveRequests) {
        this.maxKeepAliveRequests = maxKeepAliveRequests;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDaemon(boolean b) {
        this.daemon = b;
    }

    public boolean getDaemon() {
        return this.daemon;
    }

    public void setUseAsyncIO(boolean useAsyncIO) {
        this.useAsyncIO = useAsyncIO;
    }

    public boolean getUseAsyncIO() {
        return this.useAsyncIO;
    }

    public void addNegotiatedProtocol(String negotiableProtocol) {
        this.negotiableProtocols.add(negotiableProtocol);
    }

    public boolean hasNegotiableProtocols() {
        return this.negotiableProtocols.size() > 0;
    }

    public void setHandler(Handler<S> handler) {
        this.handler = handler;
    }

    public Handler<S> getHandler() {
        return this.handler;
    }

    public void setAttribute(String name, Object value) {
        if (getLog().isTraceEnabled()) {
            getLog().trace(sm.getString("endpoint.setAttribute", name, value));
        }
        this.attributes.put(name, value);
    }

    public Object getAttribute(String key) {
        Object value = this.attributes.get(key);
        if (getLog().isTraceEnabled()) {
            getLog().trace(sm.getString("endpoint.getAttribute", key, value));
        }
        return value;
    }

    public boolean setProperty(String name, String value) {
        setAttribute(name, value);
        try {
            if (name.startsWith("socket.")) {
                return IntrospectionUtils.setProperty(this.socketProperties, name.substring("socket.".length()), value);
            }
            return IntrospectionUtils.setProperty(this, name, value, false);
        } catch (Exception x) {
            getLog().error(sm.getString("endpoint.setAttributeError", name, value), x);
            return false;
        }
    }

    public String getProperty(String name) {
        Object result;
        String value = (String) getAttribute(name);
        if (value == null && name.startsWith("socket.") && (result = IntrospectionUtils.getProperty(this.socketProperties, name.substring("socket.".length()))) != null) {
            value = result.toString();
        }
        return value;
    }

    public int getCurrentThreadCount() {
        Executor executor = this.executor;
        if (executor != null) {
            if (executor instanceof org.apache.tomcat.util.threads.ThreadPoolExecutor) {
                return ((org.apache.tomcat.util.threads.ThreadPoolExecutor) executor).getPoolSize();
            }
            if (executor instanceof ResizableExecutor) {
                return ((ResizableExecutor) executor).getPoolSize();
            }
            return -1;
        }
        return -2;
    }

    public int getCurrentThreadsBusy() {
        Executor executor = this.executor;
        if (executor != null) {
            if (executor instanceof org.apache.tomcat.util.threads.ThreadPoolExecutor) {
                return ((org.apache.tomcat.util.threads.ThreadPoolExecutor) executor).getActiveCount();
            }
            if (executor instanceof ResizableExecutor) {
                return ((ResizableExecutor) executor).getActiveCount();
            }
            return -1;
        }
        return -2;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void createExecutor() {
        this.internalExecutor = true;
        TaskQueue taskqueue = new TaskQueue();
        TaskThreadFactory tf = new TaskThreadFactory(getName() + "-exec-", this.daemon, getThreadPriority());
        this.executor = new org.apache.tomcat.util.threads.ThreadPoolExecutor(getMinSpareThreads(), getMaxThreads(), 60L, TimeUnit.SECONDS, taskqueue, tf);
        taskqueue.setParent((org.apache.tomcat.util.threads.ThreadPoolExecutor) this.executor);
    }

    public void shutdownExecutor() {
        Executor executor = this.executor;
        if (executor != null && this.internalExecutor) {
            this.executor = null;
            if (executor instanceof org.apache.tomcat.util.threads.ThreadPoolExecutor) {
                org.apache.tomcat.util.threads.ThreadPoolExecutor tpe = (org.apache.tomcat.util.threads.ThreadPoolExecutor) executor;
                tpe.shutdownNow();
                long timeout = getExecutorTerminationTimeoutMillis();
                if (timeout > 0) {
                    try {
                        tpe.awaitTermination(timeout, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                    }
                    if (tpe.isTerminating()) {
                        getLog().warn(sm.getString("endpoint.warn.executorShutdown", getName()));
                    }
                }
                TaskQueue queue = (TaskQueue) tpe.getQueue();
                queue.setParent(null);
            }
        }
    }

    private void unlockAccept() {
        if (this.acceptor == null || this.acceptor.getState() != Acceptor.AcceptorState.RUNNING) {
            return;
        }
        InetSocketAddress localAddress = null;
        try {
            localAddress = getLocalAddress();
        } catch (IOException ioe) {
            getLog().debug(sm.getString("endpoint.debug.unlock.localFail", getName()), ioe);
        }
        if (localAddress == null) {
            getLog().warn(sm.getString("endpoint.debug.unlock.localNone", getName()));
            return;
        }
        try {
            InetSocketAddress unlockAddress = getUnlockAddress(localAddress);
            Socket s = new Socket();
            int stmo = 2000;
            int utmo = 2000;
            if (getSocketProperties().getSoTimeout() > 2000) {
                stmo = getSocketProperties().getSoTimeout();
            }
            if (getSocketProperties().getUnlockTimeout() > 2000) {
                utmo = getSocketProperties().getUnlockTimeout();
            }
            s.setSoTimeout(stmo);
            s.setSoLinger(getSocketProperties().getSoLingerOn(), getSocketProperties().getSoLingerTime());
            if (getLog().isDebugEnabled()) {
                getLog().debug("About to unlock socket for:" + unlockAddress);
            }
            s.connect(unlockAddress, utmo);
            if (getDeferAccept()) {
                OutputStreamWriter sw = new OutputStreamWriter(s.getOutputStream(), "ISO-8859-1");
                sw.write("OPTIONS * HTTP/1.0\r\nUser-Agent: Tomcat wakeup connection\r\n\r\n");
                sw.flush();
            }
            if (getLog().isDebugEnabled()) {
                getLog().debug("Socket unlock completed for:" + unlockAddress);
            }
            if (s != null) {
                if (0 != 0) {
                    s.close();
                } else {
                    s.close();
                }
            }
            for (long waitLeft = 1000; waitLeft > 0 && this.acceptor.getState() == Acceptor.AcceptorState.RUNNING; waitLeft -= 5) {
                Thread.sleep(5L);
            }
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            if (getLog().isDebugEnabled()) {
                getLog().debug(sm.getString("endpoint.debug.unlock.fail", String.valueOf(getPortWithOffset())), t);
            }
        }
    }

    private static InetSocketAddress getUnlockAddress(InetSocketAddress localAddress) throws SocketException {
        if (localAddress.getAddress().isAnyLocalAddress()) {
            InetAddress loopbackUnlockAddress = null;
            InetAddress linkLocalUnlockAddress = null;
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (localAddress.getAddress().getClass().isAssignableFrom(inetAddress.getClass())) {
                        if (inetAddress.isLoopbackAddress()) {
                            if (loopbackUnlockAddress == null) {
                                loopbackUnlockAddress = inetAddress;
                            }
                        } else if (inetAddress.isLinkLocalAddress()) {
                            if (linkLocalUnlockAddress == null) {
                                linkLocalUnlockAddress = inetAddress;
                            }
                        } else {
                            return new InetSocketAddress(inetAddress, localAddress.getPort());
                        }
                    }
                }
            }
            if (loopbackUnlockAddress != null) {
                return new InetSocketAddress(loopbackUnlockAddress, localAddress.getPort());
            }
            if (linkLocalUnlockAddress != null) {
                return new InetSocketAddress(linkLocalUnlockAddress, localAddress.getPort());
            }
            return new InetSocketAddress("localhost", localAddress.getPort());
        }
        return localAddress;
    }

    public boolean processSocket(SocketWrapperBase<S> socketWrapper, SocketEvent event, boolean dispatch) {
        if (socketWrapper == null) {
            return false;
        }
        try {
            SocketProcessorBase<S> sc = null;
            if (this.processorCache != null) {
                sc = this.processorCache.pop();
            }
            if (sc == null) {
                sc = createSocketProcessor(socketWrapper, event);
            } else {
                sc.reset(socketWrapper, event);
            }
            Executor executor = getExecutor();
            if (dispatch && executor != null) {
                executor.execute(sc);
            } else {
                sc.run();
            }
            return true;
        } catch (RejectedExecutionException ree) {
            getLog().warn(sm.getString("endpoint.executor.fail", socketWrapper), ree);
            return false;
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            getLog().error(sm.getString("endpoint.process.fail"), t);
            return false;
        }
    }

    private void bindWithCleanup() throws Exception {
        try {
            bind();
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            unbind();
            throw t;
        }
    }

    public final void init() throws Exception {
        SSLHostConfig[] findSslHostConfigs;
        if (this.bindOnInit) {
            bindWithCleanup();
            this.bindState = BindState.BOUND_ON_INIT;
        }
        if (this.domain != null) {
            this.oname = new ObjectName(this.domain + ":type=ThreadPool,name=\"" + getName() + "\"");
            Registry.getRegistry(null, null).registerComponent(this, this.oname, (String) null);
            ObjectName socketPropertiesOname = new ObjectName(this.domain + ":type=SocketProperties,name=\"" + getName() + "\"");
            this.socketProperties.setObjectName(socketPropertiesOname);
            Registry.getRegistry(null, null).registerComponent(this.socketProperties, socketPropertiesOname, (String) null);
            for (SSLHostConfig sslHostConfig : findSslHostConfigs()) {
                registerJmx(sslHostConfig);
            }
        }
    }

    private void registerJmx(SSLHostConfig sslHostConfig) {
        if (this.domain == null) {
            return;
        }
        try {
            ObjectName sslOname = new ObjectName(this.domain + ":type=SSLHostConfig,ThreadPool=\"" + getName() + "\",name=" + ObjectName.quote(sslHostConfig.getHostName()));
            sslHostConfig.setObjectName(sslOname);
            try {
                Registry.getRegistry(null, null).registerComponent(sslHostConfig, sslOname, (String) null);
            } catch (Exception e) {
                getLog().warn(sm.getString("endpoint.jmxRegistrationFailed", sslOname), e);
            }
        } catch (MalformedObjectNameException e2) {
            getLog().warn(sm.getString("endpoint.invalidJmxNameSslHost", sslHostConfig.getHostName()), e2);
        }
        for (SSLHostConfigCertificate sslHostConfigCert : sslHostConfig.getCertificates()) {
            try {
                ObjectName sslCertOname = new ObjectName(this.domain + ":type=SSLHostConfigCertificate,ThreadPool=\"" + getName() + "\",Host=" + ObjectName.quote(sslHostConfig.getHostName()) + ",name=" + sslHostConfigCert.getType());
                sslHostConfigCert.setObjectName(sslCertOname);
                try {
                    Registry.getRegistry(null, null).registerComponent(sslHostConfigCert, sslCertOname, (String) null);
                } catch (Exception e3) {
                    getLog().warn(sm.getString("endpoint.jmxRegistrationFailed", sslCertOname), e3);
                }
            } catch (MalformedObjectNameException e4) {
                getLog().warn(sm.getString("endpoint.invalidJmxNameSslHostCert", sslHostConfig.getHostName(), sslHostConfigCert.getType()), e4);
            }
        }
    }

    private void unregisterJmx(SSLHostConfig sslHostConfig) {
        Registry registry = Registry.getRegistry(null, null);
        registry.unregisterComponent(sslHostConfig.getObjectName());
        for (SSLHostConfigCertificate sslHostConfigCert : sslHostConfig.getCertificates()) {
            registry.unregisterComponent(sslHostConfigCert.getObjectName());
        }
    }

    public final void start() throws Exception {
        if (this.bindState == BindState.UNBOUND) {
            bindWithCleanup();
            this.bindState = BindState.BOUND_ON_START;
        }
        startInternal();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startAcceptorThread() {
        this.acceptor = new Acceptor<>(this);
        String threadName = getName() + "-Acceptor";
        this.acceptor.setThreadName(threadName);
        Thread t = new Thread(this.acceptor, threadName);
        t.setPriority(getAcceptorThreadPriority());
        t.setDaemon(getDaemon());
        t.start();
    }

    public void pause() {
        if (this.running && !this.paused) {
            this.paused = true;
            releaseConnectionLatch();
            unlockAccept();
            getHandler().pause();
        }
    }

    public void resume() {
        if (this.running) {
            this.paused = false;
        }
    }

    public final void stop() throws Exception {
        stopInternal();
        if (this.bindState == BindState.BOUND_ON_START || this.bindState == BindState.SOCKET_CLOSED_ON_STOP) {
            unbind();
            this.bindState = BindState.UNBOUND;
        }
    }

    public final void destroy() throws Exception {
        SSLHostConfig[] findSslHostConfigs;
        if (this.bindState == BindState.BOUND_ON_INIT) {
            unbind();
            this.bindState = BindState.UNBOUND;
        }
        Registry registry = Registry.getRegistry(null, null);
        registry.unregisterComponent(this.oname);
        registry.unregisterComponent(this.socketProperties.getObjectName());
        for (SSLHostConfig sslHostConfig : findSslHostConfigs()) {
            unregisterJmx(sslHostConfig);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LimitLatch initializeConnectionLatch() {
        if (this.maxConnections == -1) {
            return null;
        }
        if (this.connectionLimitLatch == null) {
            this.connectionLimitLatch = new LimitLatch(getMaxConnections());
        }
        return this.connectionLimitLatch;
    }

    private void releaseConnectionLatch() {
        LimitLatch latch = this.connectionLimitLatch;
        if (latch != null) {
            latch.releaseAll();
        }
        this.connectionLimitLatch = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void countUpOrAwaitConnection() throws InterruptedException {
        LimitLatch latch;
        if (this.maxConnections != -1 && (latch = this.connectionLimitLatch) != null) {
            latch.countUpOrAwait();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long countDownConnection() {
        LimitLatch latch;
        if (this.maxConnections != -1 && (latch = this.connectionLimitLatch) != null) {
            long result = latch.countDown();
            if (result < 0) {
                getLog().warn(sm.getString("endpoint.warn.incorrectConnectionCount"));
            }
            return result;
        }
        return -1L;
    }

    public final void closeServerSocketGraceful() {
        if (this.bindState == BindState.BOUND_ON_START) {
            this.bindState = BindState.SOCKET_CLOSED_ON_STOP;
            try {
                doCloseServerSocket();
            } catch (IOException ioe) {
                getLog().warn(sm.getString("endpoint.serverSocket.closeFailed", getName()), ioe);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void closeSocket(U socket) {
        SocketWrapperBase<S> socketWrapper = this.connections.get(socket);
        if (socketWrapper != null) {
            socketWrapper.close();
        }
    }
}
