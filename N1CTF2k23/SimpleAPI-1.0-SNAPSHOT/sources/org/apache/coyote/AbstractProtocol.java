package org.apache.coyote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;
import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
import org.apache.coyote.http11.upgrade.UpgradeProcessorInternal;
import org.apache.juli.logging.Log;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.collections.SynchronizedStack;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/AbstractProtocol.class */
public abstract class AbstractProtocol<S> implements ProtocolHandler, MBeanRegistration {
    private static final StringManager sm = StringManager.getManager(AbstractProtocol.class);
    private static final AtomicInteger nameCounter = new AtomicInteger(0);
    private final AbstractEndpoint<S, ?> endpoint;
    private AbstractEndpoint.Handler<S> handler;
    private ScheduledFuture<?> monitorFuture;
    protected Adapter adapter;
    protected String domain;
    protected ObjectName oname;
    protected MBeanServer mserver;
    protected ObjectName rgOname = null;
    private int nameIndex = 0;
    private final Set<Processor> waitingProcessors = Collections.newSetFromMap(new ConcurrentHashMap());
    private ScheduledFuture<?> timeoutFuture = null;
    protected int processorCache = 200;
    private String clientCertProvider = null;
    private int maxHeaderCount = 100;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Log getLog();

    protected abstract String getNamePrefix();

    protected abstract String getProtocolName();

    protected abstract UpgradeProtocol getNegotiatedProtocol(String str);

    protected abstract UpgradeProtocol getUpgradeProtocol(String str);

    protected abstract Processor createProcessor();

    protected abstract Processor createUpgradeProcessor(SocketWrapperBase<?> socketWrapperBase, UpgradeToken upgradeToken);

    public AbstractProtocol(AbstractEndpoint<S, ?> endpoint) {
        this.endpoint = endpoint;
        setConnectionLinger(-1);
        setTcpNoDelay(true);
    }

    public boolean setProperty(String name, String value) {
        return this.endpoint.setProperty(name, value);
    }

    public String getProperty(String name) {
        return this.endpoint.getProperty(name);
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override // org.apache.coyote.ProtocolHandler
    public Adapter getAdapter() {
        return this.adapter;
    }

    public int getProcessorCache() {
        return this.processorCache;
    }

    public void setProcessorCache(int processorCache) {
        this.processorCache = processorCache;
    }

    public String getClientCertProvider() {
        return this.clientCertProvider;
    }

    public void setClientCertProvider(String s) {
        this.clientCertProvider = s;
    }

    public int getMaxHeaderCount() {
        return this.maxHeaderCount;
    }

    public void setMaxHeaderCount(int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    @Override // org.apache.coyote.ProtocolHandler
    public boolean isAprRequired() {
        return false;
    }

    @Override // org.apache.coyote.ProtocolHandler
    public boolean isSendfileSupported() {
        return this.endpoint.getUseSendfile();
    }

    @Override // org.apache.coyote.ProtocolHandler
    public Executor getExecutor() {
        return this.endpoint.getExecutor();
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void setExecutor(Executor executor) {
        this.endpoint.setExecutor(executor);
    }

    @Override // org.apache.coyote.ProtocolHandler
    public ScheduledExecutorService getUtilityExecutor() {
        return this.endpoint.getUtilityExecutor();
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void setUtilityExecutor(ScheduledExecutorService utilityExecutor) {
        this.endpoint.setUtilityExecutor(utilityExecutor);
    }

    public int getMaxThreads() {
        return this.endpoint.getMaxThreads();
    }

    public void setMaxThreads(int maxThreads) {
        this.endpoint.setMaxThreads(maxThreads);
    }

    public int getMaxConnections() {
        return this.endpoint.getMaxConnections();
    }

    public void setMaxConnections(int maxConnections) {
        this.endpoint.setMaxConnections(maxConnections);
    }

    public int getMinSpareThreads() {
        return this.endpoint.getMinSpareThreads();
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.endpoint.setMinSpareThreads(minSpareThreads);
    }

    public int getThreadPriority() {
        return this.endpoint.getThreadPriority();
    }

    public void setThreadPriority(int threadPriority) {
        this.endpoint.setThreadPriority(threadPriority);
    }

    public int getAcceptCount() {
        return this.endpoint.getAcceptCount();
    }

    public void setAcceptCount(int acceptCount) {
        this.endpoint.setAcceptCount(acceptCount);
    }

    public boolean getTcpNoDelay() {
        return this.endpoint.getTcpNoDelay();
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.endpoint.setTcpNoDelay(tcpNoDelay);
    }

    public int getConnectionLinger() {
        return this.endpoint.getConnectionLinger();
    }

    public void setConnectionLinger(int connectionLinger) {
        this.endpoint.setConnectionLinger(connectionLinger);
    }

    public int getKeepAliveTimeout() {
        return this.endpoint.getKeepAliveTimeout();
    }

    public void setKeepAliveTimeout(int keepAliveTimeout) {
        this.endpoint.setKeepAliveTimeout(keepAliveTimeout);
    }

    public InetAddress getAddress() {
        return this.endpoint.getAddress();
    }

    public void setAddress(InetAddress ia) {
        this.endpoint.setAddress(ia);
    }

    public int getPort() {
        return this.endpoint.getPort();
    }

    public void setPort(int port) {
        this.endpoint.setPort(port);
    }

    public int getPortOffset() {
        return this.endpoint.getPortOffset();
    }

    public void setPortOffset(int portOffset) {
        this.endpoint.setPortOffset(portOffset);
    }

    public int getPortWithOffset() {
        return this.endpoint.getPortWithOffset();
    }

    public int getLocalPort() {
        return this.endpoint.getLocalPort();
    }

    public int getConnectionTimeout() {
        return this.endpoint.getConnectionTimeout();
    }

    public void setConnectionTimeout(int timeout) {
        this.endpoint.setConnectionTimeout(timeout);
    }

    public long getConnectionCount() {
        return this.endpoint.getConnectionCount();
    }

    @Deprecated
    public void setAcceptorThreadCount(int threadCount) {
    }

    @Deprecated
    public int getAcceptorThreadCount() {
        return 1;
    }

    public void setAcceptorThreadPriority(int threadPriority) {
        this.endpoint.setAcceptorThreadPriority(threadPriority);
    }

    public int getAcceptorThreadPriority() {
        return this.endpoint.getAcceptorThreadPriority();
    }

    public synchronized int getNameIndex() {
        if (this.nameIndex == 0) {
            this.nameIndex = nameCounter.incrementAndGet();
        }
        return this.nameIndex;
    }

    public String getName() {
        return ObjectName.quote(getNameInternal());
    }

    private String getNameInternal() {
        StringBuilder name = new StringBuilder(getNamePrefix());
        name.append('-');
        if (getAddress() != null) {
            name.append(getAddress().getHostAddress());
            name.append('-');
        }
        int port = getPortWithOffset();
        if (port == 0) {
            name.append("auto-");
            name.append(getNameIndex());
            int port2 = getLocalPort();
            if (port2 != -1) {
                name.append('-');
                name.append(port2);
            }
        } else {
            name.append(port);
        }
        return name.toString();
    }

    public void addWaitingProcessor(Processor processor) {
        if (getLog().isDebugEnabled()) {
            getLog().debug(sm.getString("abstractProtocol.waitingProcessor.add", processor));
        }
        this.waitingProcessors.add(processor);
    }

    public void removeWaitingProcessor(Processor processor) {
        if (getLog().isDebugEnabled()) {
            getLog().debug(sm.getString("abstractProtocol.waitingProcessor.remove", processor));
        }
        this.waitingProcessors.remove(processor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractEndpoint<S, ?> getEndpoint() {
        return this.endpoint;
    }

    protected AbstractEndpoint.Handler<S> getHandler() {
        return this.handler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setHandler(AbstractEndpoint.Handler<S> handler) {
        this.handler = handler;
    }

    public ObjectName getObjectName() {
        return this.oname;
    }

    public String getDomain() {
        return this.domain;
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        this.oname = name;
        this.mserver = server;
        this.domain = name.getDomain();
        return name;
    }

    public void postRegister(Boolean registrationDone) {
    }

    public void preDeregister() throws Exception {
    }

    public void postDeregister() {
    }

    private ObjectName createObjectName() throws MalformedObjectNameException {
        this.domain = getAdapter().getDomain();
        if (this.domain == null) {
            return null;
        }
        StringBuilder name = new StringBuilder(getDomain());
        name.append(":type=ProtocolHandler,port=");
        int port = getPortWithOffset();
        if (port > 0) {
            name.append(port);
        } else {
            name.append("auto-");
            name.append(getNameIndex());
        }
        InetAddress address = getAddress();
        if (address != null) {
            name.append(",address=");
            name.append(ObjectName.quote(address.getHostAddress()));
        }
        return new ObjectName(name.toString());
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void init() throws Exception {
        if (getLog().isInfoEnabled()) {
            getLog().info(sm.getString("abstractProtocolHandler.init", getName()));
            logPortOffset();
        }
        if (this.oname == null) {
            this.oname = createObjectName();
            if (this.oname != null) {
                Registry.getRegistry(null, null).registerComponent(this, this.oname, (String) null);
            }
        }
        if (this.domain != null) {
            this.rgOname = new ObjectName(this.domain + ":type=GlobalRequestProcessor,name=" + getName());
            Registry.getRegistry(null, null).registerComponent(getHandler().getGlobal(), this.rgOname, (String) null);
        }
        String endpointName = getName();
        this.endpoint.setName(endpointName.substring(1, endpointName.length() - 1));
        this.endpoint.setDomain(this.domain);
        this.endpoint.init();
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void start() throws Exception {
        if (getLog().isInfoEnabled()) {
            getLog().info(sm.getString("abstractProtocolHandler.start", getName()));
            logPortOffset();
        }
        this.endpoint.start();
        this.monitorFuture = getUtilityExecutor().scheduleWithFixedDelay(new Runnable() { // from class: org.apache.coyote.AbstractProtocol.1
            @Override // java.lang.Runnable
            public void run() {
                if (!AbstractProtocol.this.isPaused()) {
                    AbstractProtocol.this.startAsyncTimeout();
                }
            }
        }, 0L, 60L, TimeUnit.SECONDS);
    }

    protected void startAsyncTimeout() {
        if (this.timeoutFuture == null || (this.timeoutFuture != null && this.timeoutFuture.isDone())) {
            if (this.timeoutFuture != null && this.timeoutFuture.isDone()) {
                try {
                    this.timeoutFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    getLog().error(sm.getString("abstractProtocolHandler.asyncTimeoutError"), e);
                }
            }
            this.timeoutFuture = getUtilityExecutor().scheduleAtFixedRate(new Runnable() { // from class: org.apache.coyote.AbstractProtocol.2
                @Override // java.lang.Runnable
                public void run() {
                    long now = System.currentTimeMillis();
                    for (Processor processor : AbstractProtocol.this.waitingProcessors) {
                        processor.timeoutAsync(now);
                    }
                }
            }, 1L, 1L, TimeUnit.SECONDS);
        }
    }

    protected void stopAsyncTimeout() {
        if (this.timeoutFuture != null) {
            this.timeoutFuture.cancel(false);
            this.timeoutFuture = null;
        }
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void pause() throws Exception {
        if (getLog().isInfoEnabled()) {
            getLog().info(sm.getString("abstractProtocolHandler.pause", getName()));
        }
        stopAsyncTimeout();
        this.endpoint.pause();
    }

    public boolean isPaused() {
        return this.endpoint.isPaused();
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void resume() throws Exception {
        if (getLog().isInfoEnabled()) {
            getLog().info(sm.getString("abstractProtocolHandler.resume", getName()));
        }
        this.endpoint.resume();
        startAsyncTimeout();
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void stop() throws Exception {
        if (getLog().isInfoEnabled()) {
            getLog().info(sm.getString("abstractProtocolHandler.stop", getName()));
            logPortOffset();
        }
        if (this.monitorFuture != null) {
            this.monitorFuture.cancel(true);
            this.monitorFuture = null;
        }
        stopAsyncTimeout();
        for (Processor processor : this.waitingProcessors) {
            processor.timeoutAsync(-1L);
        }
        this.endpoint.stop();
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.coyote.ProtocolHandler
    public void destroy() throws Exception {
        if (getLog().isInfoEnabled()) {
            getLog().info(sm.getString("abstractProtocolHandler.destroy", getName()));
            logPortOffset();
        }
        try {
            this.endpoint.destroy();
            if (this.oname != null) {
                if (this.mserver == null) {
                    Registry.getRegistry(null, null).unregisterComponent(this.oname);
                } else {
                    try {
                        this.mserver.unregisterMBean(this.oname);
                    } catch (MBeanRegistrationException | InstanceNotFoundException e) {
                        getLog().info(sm.getString("abstractProtocol.mbeanDeregistrationFailed", this.oname, this.mserver));
                    }
                }
            }
            if (this.rgOname != null) {
                Registry.getRegistry(null, null).unregisterComponent(this.rgOname);
            }
        } catch (Throwable th) {
            if (this.oname != null) {
                if (this.mserver == null) {
                    Registry.getRegistry(null, null).unregisterComponent(this.oname);
                } else {
                    try {
                        this.mserver.unregisterMBean(this.oname);
                    } catch (MBeanRegistrationException | InstanceNotFoundException e2) {
                        getLog().info(sm.getString("abstractProtocol.mbeanDeregistrationFailed", this.oname, this.mserver));
                    }
                }
            }
            if (this.rgOname != null) {
                Registry.getRegistry(null, null).unregisterComponent(this.rgOname);
            }
            throw th;
        }
    }

    @Override // org.apache.coyote.ProtocolHandler
    public void closeServerSocketGraceful() {
        this.endpoint.closeServerSocketGraceful();
    }

    private void logPortOffset() {
        if (getPort() != getPortWithOffset()) {
            getLog().info(sm.getString("abstractProtocolHandler.portOffset", getName(), String.valueOf(getPort()), String.valueOf(getPortOffset())));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/AbstractProtocol$ConnectionHandler.class */
    public static class ConnectionHandler<S> implements AbstractEndpoint.Handler<S> {
        private final AbstractProtocol<S> proto;
        private final RequestGroupInfo global = new RequestGroupInfo();
        private final AtomicLong registerCount = new AtomicLong(0);
        private final RecycledProcessors recycledProcessors = new RecycledProcessors(this);

        public ConnectionHandler(AbstractProtocol<S> proto) {
            this.proto = proto;
        }

        protected AbstractProtocol<S> getProtocol() {
            return this.proto;
        }

        protected Log getLog() {
            return getProtocol().getLog();
        }

        @Override // org.apache.tomcat.util.net.AbstractEndpoint.Handler
        public Object getGlobal() {
            return this.global;
        }

        @Override // org.apache.tomcat.util.net.AbstractEndpoint.Handler
        public void recycle() {
            this.recycledProcessors.clear();
        }

        /* JADX WARN: Finally extract failed */
        @Override // org.apache.tomcat.util.net.AbstractEndpoint.Handler
        public AbstractEndpoint.Handler.SocketState process(SocketWrapperBase<S> wrapper, SocketEvent status) {
            AbstractEndpoint.Handler.SocketState state;
            UpgradeToken upgradeToken;
            ClassLoader oldCL;
            if (getLog().isDebugEnabled()) {
                getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.process", wrapper.getSocket(), status));
            }
            if (wrapper == null) {
                return AbstractEndpoint.Handler.SocketState.CLOSED;
            }
            S socket = wrapper.getSocket();
            Processor processor = (Processor) wrapper.getCurrentProcessor();
            if (getLog().isDebugEnabled()) {
                getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.connectionsGet", processor, socket));
            }
            if (SocketEvent.TIMEOUT != status || (processor != null && ((processor.isAsync() || processor.isUpgrade()) && (!processor.isAsync() || processor.checkAsyncTimeoutGeneration())))) {
                if (processor != null) {
                    getProtocol().removeWaitingProcessor(processor);
                } else if (status == SocketEvent.DISCONNECT || status == SocketEvent.ERROR) {
                    return AbstractEndpoint.Handler.SocketState.CLOSED;
                }
                ContainerThreadMarker.set();
                try {
                    if (processor == null) {
                        try {
                            try {
                                try {
                                    String negotiatedProtocol = wrapper.getNegotiatedProtocol();
                                    if (negotiatedProtocol != null && negotiatedProtocol.length() > 0) {
                                        UpgradeProtocol upgradeProtocol = getProtocol().getNegotiatedProtocol(negotiatedProtocol);
                                        if (upgradeProtocol != null) {
                                            processor = upgradeProtocol.getProcessor(wrapper, getProtocol().getAdapter());
                                            if (getLog().isDebugEnabled()) {
                                                getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.processorCreate", processor));
                                            }
                                        } else if (!negotiatedProtocol.equals("http/1.1")) {
                                            if (getLog().isDebugEnabled()) {
                                                getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.negotiatedProcessor.fail", negotiatedProtocol));
                                            }
                                            AbstractEndpoint.Handler.SocketState socketState = AbstractEndpoint.Handler.SocketState.CLOSED;
                                            ContainerThreadMarker.clear();
                                            return socketState;
                                        }
                                    }
                                } catch (OutOfMemoryError oome) {
                                    getLog().error(AbstractProtocol.sm.getString("abstractConnectionHandler.oome"), oome);
                                    ContainerThreadMarker.clear();
                                    wrapper.setCurrentProcessor(null);
                                    release(processor);
                                    return AbstractEndpoint.Handler.SocketState.CLOSED;
                                }
                            } catch (ProtocolException e) {
                                getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.protocolexception.debug"), e);
                                ContainerThreadMarker.clear();
                                wrapper.setCurrentProcessor(null);
                                release(processor);
                                return AbstractEndpoint.Handler.SocketState.CLOSED;
                            }
                        } catch (SocketException e2) {
                            getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.socketexception.debug"), e2);
                            ContainerThreadMarker.clear();
                            wrapper.setCurrentProcessor(null);
                            release(processor);
                            return AbstractEndpoint.Handler.SocketState.CLOSED;
                        } catch (IOException e3) {
                            getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.ioexception.debug"), e3);
                            ContainerThreadMarker.clear();
                            wrapper.setCurrentProcessor(null);
                            release(processor);
                            return AbstractEndpoint.Handler.SocketState.CLOSED;
                        }
                    }
                    if (processor == null) {
                        processor = this.recycledProcessors.pop();
                        if (getLog().isDebugEnabled()) {
                            getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.processorPop", processor));
                        }
                    }
                    if (processor == null) {
                        processor = getProtocol().createProcessor();
                        register(processor);
                        if (getLog().isDebugEnabled()) {
                            getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.processorCreate", processor));
                        }
                    }
                    processor.setSslSupport(wrapper.getSslSupport(getProtocol().getClientCertProvider()));
                    wrapper.setCurrentProcessor(processor);
                    AbstractEndpoint.Handler.SocketState socketState2 = AbstractEndpoint.Handler.SocketState.CLOSED;
                    do {
                        state = processor.process(wrapper, status);
                        if (state == AbstractEndpoint.Handler.SocketState.UPGRADING) {
                            upgradeToken = processor.getUpgradeToken();
                            ByteBuffer leftOverInput = processor.getLeftoverInput();
                            if (upgradeToken == null) {
                                UpgradeProtocol upgradeProtocol2 = getProtocol().getUpgradeProtocol("h2c");
                                if (upgradeProtocol2 == null) {
                                    if (getLog().isDebugEnabled()) {
                                        getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.negotiatedProcessor.fail", "h2c"));
                                    }
                                    AbstractEndpoint.Handler.SocketState socketState3 = AbstractEndpoint.Handler.SocketState.CLOSED;
                                    ContainerThreadMarker.clear();
                                    return socketState3;
                                }
                                processor = upgradeProtocol2.getProcessor(wrapper, getProtocol().getAdapter());
                                wrapper.unRead(leftOverInput);
                                wrapper.setCurrentProcessor(processor);
                            } else {
                                HttpUpgradeHandler httpUpgradeHandler = upgradeToken.getHttpUpgradeHandler();
                                release(processor);
                                processor = getProtocol().createUpgradeProcessor(wrapper, upgradeToken);
                                if (getLog().isDebugEnabled()) {
                                    getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.upgradeCreate", processor, wrapper));
                                }
                                wrapper.unRead(leftOverInput);
                                wrapper.setUpgraded(true);
                                wrapper.setCurrentProcessor(processor);
                                if (upgradeToken.getInstanceManager() == null) {
                                    httpUpgradeHandler.init((WebConnection) processor);
                                } else {
                                    oldCL = upgradeToken.getContextBind().bind(false, null);
                                    try {
                                        httpUpgradeHandler.init((WebConnection) processor);
                                        upgradeToken.getContextBind().unbind(false, oldCL);
                                    } finally {
                                        upgradeToken.getContextBind().unbind(false, oldCL);
                                    }
                                }
                                if ((httpUpgradeHandler instanceof InternalHttpUpgradeHandler) && ((InternalHttpUpgradeHandler) httpUpgradeHandler).hasAsyncIO()) {
                                    state = AbstractEndpoint.Handler.SocketState.LONG;
                                }
                            }
                        }
                    } while (state == AbstractEndpoint.Handler.SocketState.UPGRADING);
                    if (state == AbstractEndpoint.Handler.SocketState.LONG) {
                        longPoll(wrapper, processor);
                        if (processor.isAsync()) {
                            getProtocol().addWaitingProcessor(processor);
                        }
                    } else if (state == AbstractEndpoint.Handler.SocketState.OPEN) {
                        wrapper.setCurrentProcessor(null);
                        release(processor);
                        wrapper.registerReadInterest();
                    } else if (state != AbstractEndpoint.Handler.SocketState.SENDFILE) {
                        if (state == AbstractEndpoint.Handler.SocketState.UPGRADED) {
                            if (status != SocketEvent.OPEN_WRITE) {
                                longPoll(wrapper, processor);
                                getProtocol().addWaitingProcessor(processor);
                            }
                        } else if (state != AbstractEndpoint.Handler.SocketState.SUSPENDED) {
                            wrapper.setCurrentProcessor(null);
                            if (processor.isUpgrade()) {
                                upgradeToken = processor.getUpgradeToken();
                                HttpUpgradeHandler httpUpgradeHandler2 = upgradeToken.getHttpUpgradeHandler();
                                InstanceManager instanceManager = upgradeToken.getInstanceManager();
                                if (instanceManager == null) {
                                    httpUpgradeHandler2.destroy();
                                } else {
                                    oldCL = upgradeToken.getContextBind().bind(false, null);
                                    try {
                                        httpUpgradeHandler2.destroy();
                                        try {
                                            instanceManager.destroyInstance(httpUpgradeHandler2);
                                        } catch (Throwable e4) {
                                            ExceptionUtils.handleThrowable(e4);
                                            getLog().error(AbstractProtocol.sm.getString("abstractConnectionHandler.error"), e4);
                                        }
                                        upgradeToken.getContextBind().unbind(false, oldCL);
                                    } catch (Throwable th) {
                                        try {
                                            instanceManager.destroyInstance(httpUpgradeHandler2);
                                        } catch (Throwable e5) {
                                            ExceptionUtils.handleThrowable(e5);
                                            getLog().error(AbstractProtocol.sm.getString("abstractConnectionHandler.error"), e5);
                                        }
                                        throw th;
                                    }
                                }
                            }
                            release(processor);
                        }
                    }
                    AbstractEndpoint.Handler.SocketState socketState4 = state;
                    ContainerThreadMarker.clear();
                    return socketState4;
                } catch (Throwable th2) {
                    ContainerThreadMarker.clear();
                    throw th2;
                }
            }
            return AbstractEndpoint.Handler.SocketState.OPEN;
        }

        protected void longPoll(SocketWrapperBase<?> socket, Processor processor) {
            if (!processor.isAsync()) {
                socket.registerReadInterest();
            }
        }

        @Override // org.apache.tomcat.util.net.AbstractEndpoint.Handler
        public Set<S> getOpenSockets() {
            Set<SocketWrapperBase<S>> set = this.proto.getEndpoint().getConnections();
            Set<S> result = new HashSet<>();
            for (SocketWrapperBase<S> socketWrapper : set) {
                S socket = socketWrapper.getSocket();
                if (socket != null) {
                    result.add(socket);
                }
            }
            return result;
        }

        private void release(Processor processor) {
            if (processor != null) {
                processor.recycle();
                if (processor.isUpgrade()) {
                    if ((processor instanceof UpgradeProcessorInternal) && ((UpgradeProcessorInternal) processor).hasAsyncIO()) {
                        getProtocol().removeWaitingProcessor(processor);
                        return;
                    }
                    return;
                }
                this.recycledProcessors.push(processor);
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Pushed Processor [" + processor + "]");
                }
            }
        }

        @Override // org.apache.tomcat.util.net.AbstractEndpoint.Handler
        public void release(SocketWrapperBase<S> socketWrapper) {
            Processor processor = (Processor) socketWrapper.getCurrentProcessor();
            socketWrapper.setCurrentProcessor(null);
            release(processor);
        }

        protected void register(Processor processor) {
            if (getProtocol().getDomain() != null) {
                synchronized (this) {
                    try {
                        long count = this.registerCount.incrementAndGet();
                        RequestInfo rp = processor.getRequest().getRequestProcessor();
                        rp.setGlobalProcessor(this.global);
                        ObjectName rpName = new ObjectName(getProtocol().getDomain() + ":type=RequestProcessor,worker=" + getProtocol().getName() + ",name=" + getProtocol().getProtocolName() + "Request" + count);
                        if (getLog().isDebugEnabled()) {
                            getLog().debug("Register [" + processor + "] as [" + rpName + "]");
                        }
                        Registry.getRegistry(null, null).registerComponent(rp, rpName, (String) null);
                        rp.setRpName(rpName);
                    } catch (Exception e) {
                        getLog().warn(AbstractProtocol.sm.getString("abstractProtocol.processorRegisterError"), e);
                    }
                }
            }
        }

        protected void unregister(Processor processor) {
            Request r;
            if (getProtocol().getDomain() != null) {
                synchronized (this) {
                    try {
                        r = processor.getRequest();
                    } catch (Exception e) {
                        getLog().warn(AbstractProtocol.sm.getString("abstractProtocol.processorUnregisterError"), e);
                    }
                    if (r == null) {
                        return;
                    }
                    RequestInfo rp = r.getRequestProcessor();
                    rp.setGlobalProcessor(null);
                    ObjectName rpName = rp.getRpName();
                    if (getLog().isDebugEnabled()) {
                        getLog().debug("Unregister [" + rpName + "]");
                    }
                    Registry.getRegistry(null, null).unregisterComponent(rpName);
                    rp.setRpName(null);
                }
            }
        }

        @Override // org.apache.tomcat.util.net.AbstractEndpoint.Handler
        public final void pause() {
            for (SocketWrapperBase<S> wrapper : this.proto.getEndpoint().getConnections()) {
                Processor processor = (Processor) wrapper.getCurrentProcessor();
                if (processor != null) {
                    processor.pause();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/AbstractProtocol$RecycledProcessors.class */
    public static class RecycledProcessors extends SynchronizedStack<Processor> {
        private final transient ConnectionHandler<?> handler;
        protected final AtomicInteger size = new AtomicInteger(0);

        public RecycledProcessors(ConnectionHandler<?> handler) {
            this.handler = handler;
        }

        @Override // org.apache.tomcat.util.collections.SynchronizedStack
        public boolean push(Processor processor) {
            int cacheSize = this.handler.getProtocol().getProcessorCache();
            boolean offer = cacheSize == -1 ? true : this.size.get() < cacheSize;
            boolean result = false;
            if (offer) {
                result = super.push((RecycledProcessors) processor);
                if (result) {
                    this.size.incrementAndGet();
                }
            }
            if (!result) {
                this.handler.unregister(processor);
            }
            return result;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.apache.tomcat.util.collections.SynchronizedStack
        public Processor pop() {
            Processor result = (Processor) super.pop();
            if (result != null) {
                this.size.decrementAndGet();
            }
            return result;
        }

        @Override // org.apache.tomcat.util.collections.SynchronizedStack
        public synchronized void clear() {
            Processor pop = pop();
            while (true) {
                Processor next = pop;
                if (next != null) {
                    this.handler.unregister(next);
                    pop = pop();
                } else {
                    super.clear();
                    this.size.set(0);
                    return;
                }
            }
        }
    }
}
