package org.apache.tomcat.util.net;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tomcat.util.collections.SynchronizedQueue;
import org.apache.tomcat.util.collections.SynchronizedStack;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.net.jsse.JSSESupport;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioEndpoint.class */
public class NioEndpoint extends AbstractJsseEndpoint<NioChannel, SocketChannel> {
    private static final Log log = LogFactory.getLog(NioEndpoint.class);
    public static final int OP_REGISTER = 256;
    private SynchronizedStack<PollerEvent> eventCache;
    private SynchronizedStack<NioChannel> nioChannels;
    private NioSelectorPool selectorPool = new NioSelectorPool();
    private volatile ServerSocketChannel serverSock = null;
    private volatile CountDownLatch stopLatch = null;
    private boolean useInheritedChannel = false;
    private int pollerThreadPriority = 5;
    private long selectorTimeout = 1000;
    private Poller poller = null;

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public boolean setProperty(String name, String value) {
        try {
            if (name.startsWith("selectorPool.")) {
                return IntrospectionUtils.setProperty(this.selectorPool, name.substring("selectorPool.".length()), value);
            }
            return super.setProperty(name, value);
        } catch (Exception e) {
            log.error(sm.getString("endpoint.setAttributeError", name, value), e);
            return false;
        }
    }

    public void setUseInheritedChannel(boolean useInheritedChannel) {
        this.useInheritedChannel = useInheritedChannel;
    }

    public boolean getUseInheritedChannel() {
        return this.useInheritedChannel;
    }

    public void setPollerThreadPriority(int pollerThreadPriority) {
        this.pollerThreadPriority = pollerThreadPriority;
    }

    public int getPollerThreadPriority() {
        return this.pollerThreadPriority;
    }

    @Deprecated
    public void setPollerThreadCount(int pollerThreadCount) {
    }

    @Deprecated
    public int getPollerThreadCount() {
        return 1;
    }

    public void setSelectorTimeout(long timeout) {
        this.selectorTimeout = timeout;
    }

    public long getSelectorTimeout() {
        return this.selectorTimeout;
    }

    public void setSelectorPool(NioSelectorPool selectorPool) {
        this.selectorPool = selectorPool;
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public boolean getDeferAccept() {
        return false;
    }

    public int getKeepAliveCount() {
        if (this.poller == null) {
            return 0;
        }
        return this.poller.getKeyCount();
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void bind() throws Exception {
        initServerSocket();
        setStopLatch(new CountDownLatch(1));
        initialiseSsl();
        this.selectorPool.open(getName());
    }

    protected void initServerSocket() throws Exception {
        if (!getUseInheritedChannel()) {
            this.serverSock = ServerSocketChannel.open();
            this.socketProperties.setProperties(this.serverSock.socket());
            InetSocketAddress addr = new InetSocketAddress(getAddress(), getPortWithOffset());
            this.serverSock.socket().bind(addr, getAcceptCount());
        } else {
            Channel ic = System.inheritedChannel();
            if (ic instanceof ServerSocketChannel) {
                this.serverSock = (ServerSocketChannel) ic;
            }
            if (this.serverSock == null) {
                throw new IllegalArgumentException(sm.getString("endpoint.init.bind.inherited"));
            }
        }
        this.serverSock.configureBlocking(true);
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void startInternal() throws Exception {
        if (!this.running) {
            this.running = true;
            this.paused = false;
            if (this.socketProperties.getProcessorCache() != 0) {
                this.processorCache = new SynchronizedStack<>(128, this.socketProperties.getProcessorCache());
            }
            if (this.socketProperties.getEventCache() != 0) {
                this.eventCache = new SynchronizedStack<>(128, this.socketProperties.getEventCache());
            }
            if (this.socketProperties.getBufferPool() != 0) {
                this.nioChannels = new SynchronizedStack<>(128, this.socketProperties.getBufferPool());
            }
            if (getExecutor() == null) {
                createExecutor();
            }
            initializeConnectionLatch();
            this.poller = new Poller();
            Thread pollerThread = new Thread(this.poller, getName() + "-ClientPoller");
            pollerThread.setPriority(this.threadPriority);
            pollerThread.setDaemon(true);
            pollerThread.start();
            startAcceptorThread();
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void stopInternal() {
        if (!this.paused) {
            pause();
        }
        if (this.running) {
            this.running = false;
            if (this.poller != null) {
                this.poller.destroy();
                this.poller = null;
            }
            try {
                if (!getStopLatch().await(this.selectorTimeout + 100, TimeUnit.MILLISECONDS)) {
                    log.warn(sm.getString("endpoint.nio.stopLatchAwaitFail"));
                }
            } catch (InterruptedException e) {
                log.warn(sm.getString("endpoint.nio.stopLatchAwaitInterrupted"), e);
            }
            shutdownExecutor();
            if (this.eventCache != null) {
                this.eventCache.clear();
                this.eventCache = null;
            }
            if (this.nioChannels != null) {
                this.nioChannels.clear();
                this.nioChannels = null;
            }
            if (this.processorCache != null) {
                this.processorCache.clear();
                this.processorCache = null;
            }
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractJsseEndpoint, org.apache.tomcat.util.net.AbstractEndpoint
    public void unbind() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Destroy initiated for " + new InetSocketAddress(getAddress(), getPortWithOffset()));
        }
        if (this.running) {
            stop();
        }
        try {
            doCloseServerSocket();
        } catch (IOException ioe) {
            getLog().warn(sm.getString("endpoint.serverSocket.closeFailed", getName()), ioe);
        }
        destroySsl();
        super.unbind();
        if (getHandler() != null) {
            getHandler().recycle();
        }
        this.selectorPool.close();
        if (log.isDebugEnabled()) {
            log.debug("Destroy completed for " + new InetSocketAddress(getAddress(), getPortWithOffset()));
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    protected void doCloseServerSocket() throws IOException {
        if (!getUseInheritedChannel() && this.serverSock != null) {
            this.serverSock.close();
        }
        this.serverSock = null;
    }

    protected NioSelectorPool getSelectorPool() {
        return this.selectorPool;
    }

    protected SynchronizedStack<NioChannel> getNioChannels() {
        return this.nioChannels;
    }

    protected Poller getPoller() {
        return this.poller;
    }

    protected CountDownLatch getStopLatch() {
        return this.stopLatch;
    }

    protected void setStopLatch(CountDownLatch stopLatch) {
        this.stopLatch = stopLatch;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public boolean setSocketOptions(SocketChannel socket) {
        NioSocketWrapper socketWrapper = null;
        try {
            NioChannel channel = null;
            if (this.nioChannels != null) {
                channel = this.nioChannels.pop();
            }
            NioChannel nioChannel = channel;
            NioChannel channel2 = channel;
            if (nioChannel == null) {
                SocketBufferHandler bufhandler = new SocketBufferHandler(this.socketProperties.getAppReadBufSize(), this.socketProperties.getAppWriteBufSize(), this.socketProperties.getDirectBuffer());
                if (isSSLEnabled()) {
                    channel2 = new SecureNioChannel(bufhandler, this.selectorPool, this);
                } else {
                    channel2 = new NioChannel(bufhandler);
                }
            }
            NioSocketWrapper newWrapper = new NioSocketWrapper(channel2, this);
            channel2.reset(socket, newWrapper);
            this.connections.put(socket, newWrapper);
            socketWrapper = newWrapper;
            socket.configureBlocking(false);
            this.socketProperties.setProperties(socket.socket());
            socketWrapper.setReadTimeout(getConnectionTimeout());
            socketWrapper.setWriteTimeout(getConnectionTimeout());
            socketWrapper.setKeepAliveLeft(getMaxKeepAliveRequests());
            socketWrapper.setSecure(isSSLEnabled());
            this.poller.register(channel2, socketWrapper);
            return true;
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            try {
                log.error(sm.getString("endpoint.socketOptionsError"), t);
            } catch (Throwable tt) {
                ExceptionUtils.handleThrowable(tt);
            }
            if (socketWrapper == null) {
                destroySocket(socket);
                return false;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void destroySocket(SocketChannel socket) {
        countDownConnection();
        try {
            socket.close();
        } catch (IOException ioe) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("endpoint.err.close"), ioe);
            }
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractJsseEndpoint
    protected NetworkChannel getServerSocket() {
        return this.serverSock;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public SocketChannel serverSocketAccept() throws Exception {
        return this.serverSock.accept();
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    protected Log getLog() {
        return log;
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    protected SocketProcessorBase<NioChannel> createSocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event) {
        return new SocketProcessor(socketWrapper, event);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioEndpoint$PollerEvent.class */
    public static class PollerEvent {
        private NioChannel socket;
        private int interestOps;

        public PollerEvent(NioChannel ch2, int intOps) {
            reset(ch2, intOps);
        }

        public void reset(NioChannel ch2, int intOps) {
            this.socket = ch2;
            this.interestOps = intOps;
        }

        public NioChannel getSocket() {
            return this.socket;
        }

        public int getInterestOps() {
            return this.interestOps;
        }

        public void reset() {
            reset(null, 0);
        }

        public String toString() {
            return "Poller event: socket [" + this.socket + "], socketWrapper [" + this.socket.getSocketWrapper() + "], interestOps [" + this.interestOps + "]";
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioEndpoint$Poller.class */
    public class Poller implements Runnable {
        private final SynchronizedQueue<PollerEvent> events = new SynchronizedQueue<>();
        private volatile boolean close = false;
        private long nextExpiration = 0;
        private AtomicLong wakeupCounter = new AtomicLong(0);
        private volatile int keyCount = 0;
        private Selector selector = Selector.open();

        public Poller() throws IOException {
        }

        public int getKeyCount() {
            return this.keyCount;
        }

        public Selector getSelector() {
            return this.selector;
        }

        protected void destroy() {
            this.close = true;
            this.selector.wakeup();
        }

        private void addEvent(PollerEvent event) {
            this.events.offer(event);
            if (this.wakeupCounter.incrementAndGet() == 0) {
                this.selector.wakeup();
            }
        }

        public void add(NioSocketWrapper socketWrapper, int interestOps) {
            PollerEvent r = null;
            if (NioEndpoint.this.eventCache != null) {
                r = (PollerEvent) NioEndpoint.this.eventCache.pop();
            }
            if (r == null) {
                r = new PollerEvent(socketWrapper.getSocket(), interestOps);
            } else {
                r.reset(socketWrapper.getSocket(), interestOps);
            }
            addEvent(r);
            if (this.close) {
                NioEndpoint.this.processSocket(socketWrapper, SocketEvent.STOP, false);
            }
        }

        public boolean events() {
            PollerEvent pe;
            boolean result = false;
            int size = this.events.size();
            for (int i = 0; i < size && (pe = this.events.poll()) != null; i++) {
                result = true;
                NioChannel channel = pe.getSocket();
                NioSocketWrapper socketWrapper = channel.getSocketWrapper();
                int interestOps = pe.getInterestOps();
                if (interestOps == 256) {
                    try {
                        channel.getIOChannel().register(getSelector(), 1, socketWrapper);
                    } catch (Exception x) {
                        NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.nio.registerFail"), x);
                    }
                } else {
                    SelectionKey key = channel.getIOChannel().keyFor(getSelector());
                    if (key == null) {
                        socketWrapper.close();
                    } else {
                        NioSocketWrapper attachment = (NioSocketWrapper) key.attachment();
                        if (attachment != null) {
                            try {
                                int ops = key.interestOps() | interestOps;
                                attachment.interestOps(ops);
                                key.interestOps(ops);
                            } catch (CancelledKeyException e) {
                                cancelledKey(key, socketWrapper);
                            }
                        } else {
                            cancelledKey(key, attachment);
                        }
                    }
                }
                if (NioEndpoint.this.running && !NioEndpoint.this.paused && NioEndpoint.this.eventCache != null) {
                    pe.reset();
                    NioEndpoint.this.eventCache.push(pe);
                }
            }
            return result;
        }

        public void register(NioChannel socket, NioSocketWrapper socketWrapper) {
            socketWrapper.interestOps(1);
            PollerEvent event = null;
            if (NioEndpoint.this.eventCache != null) {
                event = (PollerEvent) NioEndpoint.this.eventCache.pop();
            }
            if (event == null) {
                event = new PollerEvent(socket, 256);
            } else {
                event.reset(socket, 256);
            }
            addEvent(event);
        }

        public void cancelledKey(SelectionKey sk, SocketWrapperBase<NioChannel> socketWrapper) {
            if (sk != null) {
                try {
                    sk.attach(null);
                    if (sk.isValid()) {
                        sk.cancel();
                    }
                } catch (Throwable e) {
                    try {
                        ExceptionUtils.handleThrowable(e);
                        if (NioEndpoint.log.isDebugEnabled()) {
                            NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.debug.channelCloseFail"), e);
                        }
                        if (socketWrapper != null) {
                            socketWrapper.close();
                            return;
                        }
                        return;
                    } finally {
                        if (socketWrapper != null) {
                            socketWrapper.close();
                        }
                    }
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                boolean hasEvents = false;
                try {
                    if (!this.close) {
                        hasEvents = events();
                        if (this.wakeupCounter.getAndSet(-1L) > 0) {
                            this.keyCount = this.selector.selectNow();
                        } else {
                            this.keyCount = this.selector.select(NioEndpoint.this.selectorTimeout);
                        }
                        this.wakeupCounter.set(0L);
                    }
                } catch (Throwable x) {
                    ExceptionUtils.handleThrowable(x);
                    NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.nio.selectorLoopError"), x);
                }
                if (this.close) {
                    events();
                    timeout(0, false);
                    try {
                        this.selector.close();
                        break;
                    } catch (IOException ioe) {
                        NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.nio.selectorCloseFail"), ioe);
                        break;
                    }
                }
                if (this.keyCount == 0) {
                    hasEvents |= events();
                }
                Iterator<SelectionKey> iterator = this.keyCount > 0 ? this.selector.selectedKeys().iterator() : null;
                while (iterator != null && iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    NioSocketWrapper socketWrapper = (NioSocketWrapper) sk.attachment();
                    if (socketWrapper == null) {
                        iterator.remove();
                    } else {
                        iterator.remove();
                        processKey(sk, socketWrapper);
                    }
                }
                timeout(this.keyCount, hasEvents);
            }
            NioEndpoint.this.getStopLatch().countDown();
        }

        protected void processKey(SelectionKey sk, NioSocketWrapper socketWrapper) {
            try {
                if (this.close) {
                    cancelledKey(sk, socketWrapper);
                } else if (sk.isValid() && socketWrapper != null) {
                    if (sk.isReadable() || sk.isWritable()) {
                        if (socketWrapper.getSendfileData() != null) {
                            processSendfile(sk, socketWrapper, false);
                        } else {
                            unreg(sk, socketWrapper, sk.readyOps());
                            boolean closeSocket = false;
                            if (sk.isReadable()) {
                                if (socketWrapper.readOperation != null) {
                                    if (!socketWrapper.readOperation.process()) {
                                        closeSocket = true;
                                    }
                                } else if (!NioEndpoint.this.processSocket(socketWrapper, SocketEvent.OPEN_READ, true)) {
                                    closeSocket = true;
                                }
                            }
                            if (!closeSocket && sk.isWritable()) {
                                if (socketWrapper.writeOperation != null) {
                                    if (!socketWrapper.writeOperation.process()) {
                                        closeSocket = true;
                                    }
                                } else if (!NioEndpoint.this.processSocket(socketWrapper, SocketEvent.OPEN_WRITE, true)) {
                                    closeSocket = true;
                                }
                            }
                            if (closeSocket) {
                                cancelledKey(sk, socketWrapper);
                            }
                        }
                    }
                } else {
                    cancelledKey(sk, socketWrapper);
                }
            } catch (CancelledKeyException e) {
                cancelledKey(sk, socketWrapper);
            } catch (Throwable t) {
                ExceptionUtils.handleThrowable(t);
                NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.nio.keyProcessingError"), t);
            }
        }

        public SendfileState processSendfile(SelectionKey sk, NioSocketWrapper socketWrapper, boolean calledByProcessor) {
            try {
                unreg(sk, socketWrapper, sk.readyOps());
                SendfileData sd = socketWrapper.getSendfileData();
                if (NioEndpoint.log.isTraceEnabled()) {
                    NioEndpoint.log.trace("Processing send file for: " + sd.fileName);
                }
                if (sd.fchannel == null) {
                    File f = new File(sd.fileName);
                    FileInputStream fis = new FileInputStream(f);
                    sd.fchannel = fis.getChannel();
                }
                NioChannel sc = socketWrapper.getSocket();
                WritableByteChannel wc = sc instanceof SecureNioChannel ? sc : sc.getIOChannel();
                if (sc.getOutboundRemaining() > 0) {
                    if (sc.flushOutbound()) {
                        socketWrapper.updateLastWrite();
                    }
                } else {
                    long written = sd.fchannel.transferTo(sd.pos, sd.length, wc);
                    if (written > 0) {
                        sd.pos += written;
                        sd.length -= written;
                        socketWrapper.updateLastWrite();
                    } else if (sd.fchannel.size() <= sd.pos) {
                        throw new IOException(AbstractEndpoint.sm.getString("endpoint.sendfile.tooMuchData"));
                    }
                }
                if (sd.length > 0 || sc.getOutboundRemaining() > 0) {
                    if (NioEndpoint.log.isDebugEnabled()) {
                        NioEndpoint.log.debug("OP_WRITE for sendfile: " + sd.fileName);
                    }
                    if (calledByProcessor) {
                        add(socketWrapper, 4);
                    } else {
                        reg(sk, socketWrapper, 4);
                    }
                    return SendfileState.PENDING;
                }
                if (NioEndpoint.log.isDebugEnabled()) {
                    NioEndpoint.log.debug("Send file complete for: " + sd.fileName);
                }
                socketWrapper.setSendfileData(null);
                try {
                    sd.fchannel.close();
                } catch (Exception e) {
                }
                if (!calledByProcessor) {
                    switch (sd.keepAliveState) {
                        case NONE:
                            if (NioEndpoint.log.isDebugEnabled()) {
                                NioEndpoint.log.debug("Send file connection is being closed");
                            }
                            NioEndpoint.this.poller.cancelledKey(sk, socketWrapper);
                            break;
                        case PIPELINED:
                            if (NioEndpoint.log.isDebugEnabled()) {
                                NioEndpoint.log.debug("Connection is keep alive, processing pipe-lined data");
                            }
                            if (!NioEndpoint.this.processSocket(socketWrapper, SocketEvent.OPEN_READ, true)) {
                                NioEndpoint.this.poller.cancelledKey(sk, socketWrapper);
                                break;
                            }
                            break;
                        case OPEN:
                            if (NioEndpoint.log.isDebugEnabled()) {
                                NioEndpoint.log.debug("Connection is keep alive, registering back for OP_READ");
                            }
                            reg(sk, socketWrapper, 1);
                            break;
                    }
                }
                return SendfileState.DONE;
            } catch (IOException e2) {
                if (NioEndpoint.log.isDebugEnabled()) {
                    NioEndpoint.log.debug("Unable to complete sendfile request:", e2);
                }
                if (!calledByProcessor && 0 != 0) {
                    NioEndpoint.this.poller.cancelledKey(sk, socketWrapper);
                }
                return SendfileState.ERROR;
            } catch (Throwable t) {
                NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.sendfile.error"), t);
                if (!calledByProcessor && 0 != 0) {
                    NioEndpoint.this.poller.cancelledKey(sk, socketWrapper);
                }
                return SendfileState.ERROR;
            }
        }

        protected void unreg(SelectionKey sk, NioSocketWrapper socketWrapper, int readyOps) {
            reg(sk, socketWrapper, sk.interestOps() & (readyOps ^ (-1)));
        }

        protected void reg(SelectionKey sk, NioSocketWrapper socketWrapper, int intops) {
            sk.interestOps(intops);
            socketWrapper.interestOps(intops);
        }

        protected void timeout(int keyCount, boolean hasEvents) {
            long now = System.currentTimeMillis();
            if (this.nextExpiration > 0 && ((keyCount > 0 || hasEvents) && now < this.nextExpiration && !this.close)) {
                return;
            }
            int keycount = 0;
            try {
                for (SelectionKey key : this.selector.keys()) {
                    keycount++;
                    try {
                        NioSocketWrapper socketWrapper = (NioSocketWrapper) key.attachment();
                        if (socketWrapper == null) {
                            cancelledKey(key, null);
                        } else if (this.close) {
                            key.interestOps(0);
                            socketWrapper.interestOps(0);
                            cancelledKey(key, socketWrapper);
                        } else if ((socketWrapper.interestOps() & 1) == 1 || (socketWrapper.interestOps() & 4) == 4) {
                            boolean readTimeout = false;
                            boolean writeTimeout = false;
                            if ((socketWrapper.interestOps() & 1) == 1) {
                                long delta = now - socketWrapper.getLastRead();
                                long timeout = socketWrapper.getReadTimeout();
                                if (timeout > 0 && delta > timeout) {
                                    readTimeout = true;
                                }
                            }
                            if (!readTimeout && (socketWrapper.interestOps() & 4) == 4) {
                                long delta2 = now - socketWrapper.getLastWrite();
                                long timeout2 = socketWrapper.getWriteTimeout();
                                if (timeout2 > 0 && delta2 > timeout2) {
                                    writeTimeout = true;
                                }
                            }
                            if (readTimeout || writeTimeout) {
                                key.interestOps(0);
                                socketWrapper.interestOps(0);
                                socketWrapper.setError(new SocketTimeoutException());
                                if (readTimeout && socketWrapper.readOperation != null) {
                                    if (!socketWrapper.readOperation.process()) {
                                        cancelledKey(key, socketWrapper);
                                    }
                                } else if (writeTimeout && socketWrapper.writeOperation != null) {
                                    if (!socketWrapper.writeOperation.process()) {
                                        cancelledKey(key, socketWrapper);
                                    }
                                } else if (!NioEndpoint.this.processSocket(socketWrapper, SocketEvent.ERROR, true)) {
                                    cancelledKey(key, socketWrapper);
                                }
                            }
                        }
                    } catch (CancelledKeyException e) {
                        cancelledKey(key, (NioSocketWrapper) key.attachment());
                    }
                }
            } catch (ConcurrentModificationException cme) {
                NioEndpoint.log.warn(AbstractEndpoint.sm.getString("endpoint.nio.timeoutCme"), cme);
            }
            long prevExp = this.nextExpiration;
            this.nextExpiration = System.currentTimeMillis() + NioEndpoint.this.socketProperties.getTimeoutInterval();
            if (NioEndpoint.log.isTraceEnabled()) {
                NioEndpoint.log.trace("timeout completed: keys processed=" + keycount + "; now=" + now + "; nextExpiration=" + prevExp + "; keyCount=" + keyCount + "; hasEvents=" + hasEvents + "; eval=" + (now < prevExp && (keyCount > 0 || hasEvents) && !this.close));
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioEndpoint$NioSocketWrapper.class */
    public static class NioSocketWrapper extends SocketWrapperBase<NioChannel> {
        private final NioSelectorPool pool;
        private final SynchronizedStack<NioChannel> nioChannels;
        private final Poller poller;
        private int interestOps;
        private CountDownLatch readLatch;
        private CountDownLatch writeLatch;
        private volatile SendfileData sendfileData;
        private volatile long lastRead;
        private volatile long lastWrite;

        public NioSocketWrapper(NioChannel channel, NioEndpoint endpoint) {
            super(channel, endpoint);
            this.interestOps = 0;
            this.readLatch = null;
            this.writeLatch = null;
            this.sendfileData = null;
            this.lastRead = System.currentTimeMillis();
            this.lastWrite = this.lastRead;
            this.pool = endpoint.getSelectorPool();
            this.nioChannels = endpoint.getNioChannels();
            this.poller = endpoint.getPoller();
            this.socketBufferHandler = channel.getBufHandler();
        }

        public Poller getPoller() {
            return this.poller;
        }

        public int interestOps() {
            return this.interestOps;
        }

        public int interestOps(int ops) {
            this.interestOps = ops;
            return ops;
        }

        public CountDownLatch getReadLatch() {
            return this.readLatch;
        }

        public CountDownLatch getWriteLatch() {
            return this.writeLatch;
        }

        protected CountDownLatch resetLatch(CountDownLatch latch) {
            if (latch == null || latch.getCount() == 0) {
                return null;
            }
            throw new IllegalStateException(sm.getString("endpoint.nio.latchMustBeZero"));
        }

        public void resetReadLatch() {
            this.readLatch = resetLatch(this.readLatch);
        }

        public void resetWriteLatch() {
            this.writeLatch = resetLatch(this.writeLatch);
        }

        protected CountDownLatch startLatch(CountDownLatch latch, int cnt) {
            if (latch == null || latch.getCount() == 0) {
                return new CountDownLatch(cnt);
            }
            throw new IllegalStateException(sm.getString("endpoint.nio.latchMustBeZero"));
        }

        public void startReadLatch(int cnt) {
            this.readLatch = startLatch(this.readLatch, cnt);
        }

        public void startWriteLatch(int cnt) {
            this.writeLatch = startLatch(this.writeLatch, cnt);
        }

        protected void awaitLatch(CountDownLatch latch, long timeout, TimeUnit unit) throws InterruptedException {
            if (latch == null) {
                throw new IllegalStateException(sm.getString("endpoint.nio.nullLatch"));
            }
            latch.await(timeout, unit);
        }

        public void awaitReadLatch(long timeout, TimeUnit unit) throws InterruptedException {
            awaitLatch(this.readLatch, timeout, unit);
        }

        public void awaitWriteLatch(long timeout, TimeUnit unit) throws InterruptedException {
            awaitLatch(this.writeLatch, timeout, unit);
        }

        public void setSendfileData(SendfileData sf) {
            this.sendfileData = sf;
        }

        public SendfileData getSendfileData() {
            return this.sendfileData;
        }

        public void updateLastWrite() {
            this.lastWrite = System.currentTimeMillis();
        }

        public long getLastWrite() {
            return this.lastWrite;
        }

        public void updateLastRead() {
            this.lastRead = System.currentTimeMillis();
        }

        public long getLastRead() {
            return this.lastRead;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean isReadyForRead() throws IOException {
            this.socketBufferHandler.configureReadBufferForRead();
            if (this.socketBufferHandler.getReadBuffer().remaining() > 0) {
                return true;
            }
            fillReadBuffer(false);
            boolean isReady = this.socketBufferHandler.getReadBuffer().position() > 0;
            return isReady;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public int read(boolean block, byte[] b, int off, int len) throws IOException {
            int nRead = populateReadBuffer(b, off, len);
            if (nRead > 0) {
                return nRead;
            }
            int nRead2 = fillReadBuffer(block);
            updateLastRead();
            if (nRead2 > 0) {
                this.socketBufferHandler.configureReadBufferForRead();
                nRead2 = Math.min(nRead2, len);
                this.socketBufferHandler.getReadBuffer().get(b, off, nRead2);
            }
            return nRead2;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public int read(boolean block, ByteBuffer to) throws IOException {
            int nRead;
            int nRead2 = populateReadBuffer(to);
            if (nRead2 > 0) {
                return nRead2;
            }
            int limit = this.socketBufferHandler.getReadBuffer().capacity();
            if (to.remaining() >= limit) {
                to.limit(to.position() + limit);
                nRead = fillReadBuffer(block, to);
                if (NioEndpoint.log.isDebugEnabled()) {
                    NioEndpoint.log.debug("Socket: [" + this + "], Read direct from socket: [" + nRead + "]");
                }
                updateLastRead();
            } else {
                nRead = fillReadBuffer(block);
                if (NioEndpoint.log.isDebugEnabled()) {
                    NioEndpoint.log.debug("Socket: [" + this + "], Read into buffer: [" + nRead + "]");
                }
                updateLastRead();
                if (nRead > 0) {
                    nRead = populateReadBuffer(to);
                }
            }
            return nRead;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void doClose() {
            if (NioEndpoint.log.isDebugEnabled()) {
                NioEndpoint.log.debug("Calling [" + getEndpoint() + "].closeSocket([" + this + "])");
            }
            try {
                getEndpoint().connections.remove(getSocket().getIOChannel());
                if (getSocket().isOpen()) {
                    getSocket().close(true);
                }
                if (getEndpoint().running && !getEndpoint().paused && (this.nioChannels == null || !this.nioChannels.push(getSocket()))) {
                    getSocket().free();
                }
            } catch (Throwable e) {
                try {
                    ExceptionUtils.handleThrowable(e);
                    if (NioEndpoint.log.isDebugEnabled()) {
                        NioEndpoint.log.error(sm.getString("endpoint.debug.channelCloseFail"), e);
                    }
                } finally {
                    this.socketBufferHandler = SocketBufferHandler.EMPTY;
                    this.nonBlockingWriteBuffer.clear();
                    reset(NioChannel.CLOSED_NIO_CHANNEL);
                }
            }
            try {
                SendfileData data = getSendfileData();
                if (data != null && data.fchannel != null && data.fchannel.isOpen()) {
                    data.fchannel.close();
                }
            } catch (Throwable e2) {
                ExceptionUtils.handleThrowable(e2);
                if (NioEndpoint.log.isDebugEnabled()) {
                    NioEndpoint.log.error(sm.getString("endpoint.sendfile.closeError"), e2);
                }
            }
        }

        private int fillReadBuffer(boolean block) throws IOException {
            this.socketBufferHandler.configureReadBufferForWrite();
            return fillReadBuffer(block, this.socketBufferHandler.getReadBuffer());
        }

        /* JADX WARN: Finally extract failed */
        private int fillReadBuffer(boolean block, ByteBuffer to) throws IOException {
            int nRead;
            NioChannel socket = getSocket();
            if (socket == NioChannel.CLOSED_NIO_CHANNEL) {
                throw new ClosedChannelException();
            }
            if (block) {
                Selector selector = null;
                try {
                    selector = this.pool.get();
                } catch (IOException e) {
                }
                try {
                    nRead = this.pool.read(to, socket, selector, getReadTimeout());
                    if (selector != null) {
                        this.pool.put(selector);
                    }
                } catch (Throwable th) {
                    if (selector != null) {
                        this.pool.put(selector);
                    }
                    throw th;
                }
            } else {
                nRead = socket.read(to);
                if (nRead == -1) {
                    throw new EOFException();
                }
            }
            return nRead;
        }

        /* JADX WARN: Finally extract failed */
        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void doWrite(boolean block, ByteBuffer from) throws IOException {
            NioChannel socket = getSocket();
            if (socket == NioChannel.CLOSED_NIO_CHANNEL) {
                throw new ClosedChannelException();
            }
            if (!block) {
                do {
                    int n = socket.write(from);
                    if (n == -1) {
                        throw new EOFException();
                    }
                    if (n <= 0) {
                        break;
                    }
                } while (from.hasRemaining());
            } else {
                long writeTimeout = getWriteTimeout();
                Selector selector = null;
                try {
                    selector = this.pool.get();
                } catch (IOException e) {
                }
                try {
                    this.pool.write(from, socket, selector, writeTimeout);
                    do {
                    } while (!socket.flush(true, selector, writeTimeout));
                    if (selector != null) {
                        this.pool.put(selector);
                    }
                } catch (Throwable th) {
                    if (selector != null) {
                        this.pool.put(selector);
                    }
                    throw th;
                }
            }
            updateLastWrite();
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void registerReadInterest() {
            if (NioEndpoint.log.isDebugEnabled()) {
                NioEndpoint.log.debug(sm.getString("endpoint.debug.registerRead", this));
            }
            getPoller().add(this, 1);
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void registerWriteInterest() {
            if (NioEndpoint.log.isDebugEnabled()) {
                NioEndpoint.log.debug(sm.getString("endpoint.debug.registerWrite", this));
            }
            getPoller().add(this, 4);
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public SendfileDataBase createSendfileData(String filename, long pos, long length) {
            return new SendfileData(filename, pos, length);
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public SendfileState processSendfile(SendfileDataBase sendfileData) {
            setSendfileData((SendfileData) sendfileData);
            SelectionKey key = getSocket().getIOChannel().keyFor(getPoller().getSelector());
            return getPoller().processSendfile(key, this, true);
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateRemoteAddr() {
            InetAddress inetAddr;
            SocketChannel sc = getSocket().getIOChannel();
            if (sc != null && (inetAddr = sc.socket().getInetAddress()) != null) {
                this.remoteAddr = inetAddr.getHostAddress();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateRemoteHost() {
            InetAddress inetAddr;
            SocketChannel sc = getSocket().getIOChannel();
            if (sc != null && (inetAddr = sc.socket().getInetAddress()) != null) {
                this.remoteHost = inetAddr.getHostName();
                if (this.remoteAddr == null) {
                    this.remoteAddr = inetAddr.getHostAddress();
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateRemotePort() {
            SocketChannel sc = getSocket().getIOChannel();
            if (sc != null) {
                this.remotePort = sc.socket().getPort();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateLocalName() {
            InetAddress inetAddr;
            SocketChannel sc = getSocket().getIOChannel();
            if (sc != null && (inetAddr = sc.socket().getLocalAddress()) != null) {
                this.localName = inetAddr.getHostName();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateLocalAddr() {
            InetAddress inetAddr;
            SocketChannel sc = getSocket().getIOChannel();
            if (sc != null && (inetAddr = sc.socket().getLocalAddress()) != null) {
                this.localAddr = inetAddr.getHostAddress();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateLocalPort() {
            SocketChannel sc = getSocket().getIOChannel();
            if (sc != null) {
                this.localPort = sc.socket().getLocalPort();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public SSLSupport getSslSupport(String clientCertProvider) {
            if (getSocket() instanceof SecureNioChannel) {
                SecureNioChannel ch2 = (SecureNioChannel) getSocket();
                SSLEngine sslEngine = ch2.getSslEngine();
                if (sslEngine != null) {
                    SSLSession session = sslEngine.getSession();
                    return ((NioEndpoint) getEndpoint()).getSslImplementation().getSSLSupport(session);
                }
                return null;
            }
            return null;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void doClientAuth(SSLSupport sslSupport) throws IOException {
            SecureNioChannel sslChannel = (SecureNioChannel) getSocket();
            SSLEngine engine = sslChannel.getSslEngine();
            if (!engine.getNeedClientAuth()) {
                engine.setNeedClientAuth(true);
                sslChannel.rehandshake(getEndpoint().getConnectionTimeout());
                ((JSSESupport) sslSupport).setSession(engine.getSession());
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void setAppReadBufHandler(ApplicationBufferHandler handler) {
            getSocket().setAppReadBufHandler(handler);
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected <A> SocketWrapperBase<NioChannel>.OperationState<A> newOperationState(boolean read, ByteBuffer[] buffers, int offset, int length, SocketWrapperBase.BlockingMode block, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler, Semaphore semaphore, SocketWrapperBase<NioChannel>.VectoredIOCompletionHandler<A> completion) {
            return new NioOperationState(read, buffers, offset, length, block, timeout, unit, attachment, check, handler, semaphore, completion);
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioEndpoint$NioSocketWrapper$NioOperationState.class */
        private class NioOperationState<A> extends SocketWrapperBase<NioChannel>.OperationState<A> {
            private volatile boolean inline;

            private NioOperationState(boolean read, ByteBuffer[] buffers, int offset, int length, SocketWrapperBase.BlockingMode block, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler, Semaphore semaphore, SocketWrapperBase<NioChannel>.VectoredIOCompletionHandler<A> completion) {
                super(read, buffers, offset, length, block, timeout, unit, attachment, check, handler, semaphore, completion);
                this.inline = true;
            }

            @Override // org.apache.tomcat.util.net.SocketWrapperBase.OperationState
            protected boolean isInline() {
                return this.inline;
            }

            @Override // java.lang.Runnable
            public void run() {
                long n;
                long nBytes = 0;
                if (NioSocketWrapper.this.getError() == null) {
                    try {
                        synchronized (this) {
                            if (!this.completionDone) {
                                if (NioEndpoint.log.isDebugEnabled()) {
                                    NioEndpoint.log.debug("Skip concurrent " + (this.read ? "read" : "write") + " notification");
                                }
                                return;
                            }
                            if (this.read) {
                                if (!NioSocketWrapper.this.socketBufferHandler.isReadBufferEmpty()) {
                                    NioSocketWrapper.this.socketBufferHandler.configureReadBufferForRead();
                                    for (int i = 0; i < this.length && !NioSocketWrapper.this.socketBufferHandler.isReadBufferEmpty(); i++) {
                                        nBytes += SocketWrapperBase.transfer(NioSocketWrapper.this.socketBufferHandler.getReadBuffer(), this.buffers[this.offset + i]);
                                    }
                                }
                                if (nBytes == 0) {
                                    nBytes = NioSocketWrapper.this.getSocket().read(this.buffers, this.offset, this.length);
                                    NioSocketWrapper.this.updateLastRead();
                                }
                            } else {
                                boolean doWrite = true;
                                if (!NioSocketWrapper.this.socketBufferHandler.isWriteBufferEmpty()) {
                                    NioSocketWrapper.this.socketBufferHandler.configureWriteBufferForRead();
                                    do {
                                        nBytes = NioSocketWrapper.this.getSocket().write(NioSocketWrapper.this.socketBufferHandler.getWriteBuffer());
                                        if (NioSocketWrapper.this.socketBufferHandler.isWriteBufferEmpty()) {
                                            break;
                                        }
                                    } while (nBytes > 0);
                                    if (!NioSocketWrapper.this.socketBufferHandler.isWriteBufferEmpty()) {
                                        doWrite = false;
                                    }
                                    if (nBytes > 0) {
                                        nBytes = 0;
                                    }
                                }
                                if (doWrite) {
                                    do {
                                        n = NioSocketWrapper.this.getSocket().write(this.buffers, this.offset, this.length);
                                        if (n == -1) {
                                            nBytes = n;
                                        } else {
                                            nBytes += n;
                                        }
                                    } while (n > 0);
                                    NioSocketWrapper.this.updateLastWrite();
                                }
                            }
                            if (nBytes != 0 || !SocketWrapperBase.buffersArrayHasRemaining(this.buffers, this.offset, this.length)) {
                                this.completionDone = false;
                            }
                        }
                    } catch (IOException e) {
                        NioSocketWrapper.this.setError(e);
                    }
                }
                if (nBytes > 0 || (nBytes == 0 && !SocketWrapperBase.buffersArrayHasRemaining(this.buffers, this.offset, this.length))) {
                    this.completion.completed(Long.valueOf(nBytes), (SocketWrapperBase.OperationState) this);
                } else if (nBytes < 0 || NioSocketWrapper.this.getError() != null) {
                    IOException error = NioSocketWrapper.this.getError();
                    if (error == null) {
                        error = new EOFException();
                    }
                    this.completion.failed((Throwable) error, (SocketWrapperBase.OperationState) this);
                } else {
                    this.inline = false;
                    if (this.read) {
                        NioSocketWrapper.this.registerReadInterest();
                    } else {
                        NioSocketWrapper.this.registerWriteInterest();
                    }
                }
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioEndpoint$SocketProcessor.class */
    protected class SocketProcessor extends SocketProcessorBase<NioChannel> {
        public SocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event) {
            super(socketWrapper, event);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.tomcat.util.net.SocketProcessorBase
        protected void doRun() {
            int handshake;
            NioChannel socket = (NioChannel) this.socketWrapper.getSocket();
            Poller poller = NioEndpoint.this.poller;
            try {
                if (poller == null) {
                    this.socketWrapper.close();
                    return;
                }
                try {
                    try {
                        if (socket.isHandshakeComplete()) {
                            handshake = 0;
                        } else if (this.event == SocketEvent.STOP || this.event == SocketEvent.DISCONNECT || this.event == SocketEvent.ERROR) {
                            handshake = -1;
                        } else {
                            handshake = socket.handshake(this.event == SocketEvent.OPEN_READ, this.event == SocketEvent.OPEN_WRITE);
                            this.event = SocketEvent.OPEN_READ;
                        }
                    } catch (IOException x) {
                        handshake = -1;
                        if (NioEndpoint.log.isDebugEnabled()) {
                            NioEndpoint.log.debug("Error during SSL handshake", x);
                        }
                    } catch (CancelledKeyException e) {
                        handshake = -1;
                    }
                    if (handshake == 0) {
                        AbstractEndpoint.Handler.SocketState socketState = AbstractEndpoint.Handler.SocketState.OPEN;
                        AbstractEndpoint.Handler.SocketState state = this.event == null ? NioEndpoint.this.getHandler().process(this.socketWrapper, SocketEvent.OPEN_READ) : NioEndpoint.this.getHandler().process(this.socketWrapper, this.event);
                        if (state == AbstractEndpoint.Handler.SocketState.CLOSED) {
                            poller.cancelledKey(socket.getIOChannel().keyFor(poller.getSelector()), this.socketWrapper);
                        }
                    } else if (handshake == -1) {
                        NioEndpoint.this.getHandler().process(this.socketWrapper, SocketEvent.CONNECT_FAIL);
                        poller.cancelledKey(socket.getIOChannel().keyFor(poller.getSelector()), this.socketWrapper);
                    } else if (handshake == 1) {
                        this.socketWrapper.registerReadInterest();
                    } else if (handshake == 4) {
                        this.socketWrapper.registerWriteInterest();
                    }
                    this.socketWrapper = null;
                    this.event = null;
                    if (!NioEndpoint.this.running || NioEndpoint.this.paused || NioEndpoint.this.processorCache == null) {
                        return;
                    }
                    NioEndpoint.this.processorCache.push(this);
                } catch (VirtualMachineError vme) {
                    ExceptionUtils.handleThrowable(vme);
                    this.socketWrapper = null;
                    this.event = null;
                    if (!NioEndpoint.this.running || NioEndpoint.this.paused || NioEndpoint.this.processorCache == null) {
                        return;
                    }
                    NioEndpoint.this.processorCache.push(this);
                } catch (CancelledKeyException e2) {
                    poller.cancelledKey(socket.getIOChannel().keyFor(poller.getSelector()), this.socketWrapper);
                    this.socketWrapper = null;
                    this.event = null;
                    if (!NioEndpoint.this.running || NioEndpoint.this.paused || NioEndpoint.this.processorCache == null) {
                        return;
                    }
                    NioEndpoint.this.processorCache.push(this);
                } catch (Throwable t) {
                    NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.processing.fail"), t);
                    poller.cancelledKey(socket.getIOChannel().keyFor(poller.getSelector()), this.socketWrapper);
                    this.socketWrapper = null;
                    this.event = null;
                    if (!NioEndpoint.this.running || NioEndpoint.this.paused || NioEndpoint.this.processorCache == null) {
                        return;
                    }
                    NioEndpoint.this.processorCache.push(this);
                }
            } catch (Throwable th) {
                this.socketWrapper = null;
                this.event = null;
                if (NioEndpoint.this.running && !NioEndpoint.this.paused && NioEndpoint.this.processorCache != null) {
                    NioEndpoint.this.processorCache.push(this);
                }
                throw th;
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioEndpoint$SendfileData.class */
    public static class SendfileData extends SendfileDataBase {
        protected volatile FileChannel fchannel;

        public SendfileData(String filename, long pos, long length) {
            super(filename, pos, length);
        }
    }
}
