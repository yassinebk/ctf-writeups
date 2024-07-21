package org.apache.tomcat.util.net;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.WritePendingException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.ByteBufferUtils;
import org.apache.tomcat.util.compat.JreCompat;
import org.apache.tomcat.util.net.SSLUtil;
import org.apache.tomcat.util.net.TLSClientHelloExtractor;
import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel.class */
public class SecureNio2Channel extends Nio2Channel {
    private static final Log log = LogFactory.getLog(SecureNio2Channel.class);
    private static final StringManager sm = StringManager.getManager(SecureNio2Channel.class);
    private static final int DEFAULT_NET_BUFFER_SIZE = 16921;
    protected final Nio2Endpoint endpoint;
    protected ByteBuffer netInBuffer;
    protected ByteBuffer netOutBuffer;
    protected SSLEngine sslEngine;
    protected boolean sniComplete;
    private volatile boolean handshakeComplete;
    private volatile SSLEngineResult.HandshakeStatus handshakeStatus;
    protected boolean closed;
    protected boolean closing;
    private volatile boolean unwrapBeforeRead;
    private final CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> handshakeReadCompletionHandler;
    private final CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> handshakeWriteCompletionHandler;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel$OverflowState.class */
    public enum OverflowState {
        NONE,
        PROCESSING,
        DONE
    }

    public SecureNio2Channel(SocketBufferHandler bufHandler, Nio2Endpoint endpoint) {
        super(bufHandler);
        this.sniComplete = false;
        this.handshakeComplete = false;
        this.endpoint = endpoint;
        if (endpoint.getSocketProperties().getDirectSslBuffer()) {
            this.netInBuffer = ByteBuffer.allocateDirect(DEFAULT_NET_BUFFER_SIZE);
            this.netOutBuffer = ByteBuffer.allocateDirect(DEFAULT_NET_BUFFER_SIZE);
        } else {
            this.netInBuffer = ByteBuffer.allocate(DEFAULT_NET_BUFFER_SIZE);
            this.netOutBuffer = ByteBuffer.allocate(DEFAULT_NET_BUFFER_SIZE);
        }
        this.handshakeReadCompletionHandler = new HandshakeReadCompletionHandler();
        this.handshakeWriteCompletionHandler = new HandshakeWriteCompletionHandler();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel$HandshakeReadCompletionHandler.class */
    private class HandshakeReadCompletionHandler implements CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> {
        private HandshakeReadCompletionHandler() {
        }

        @Override // java.nio.channels.CompletionHandler
        public void completed(Integer result, SocketWrapperBase<Nio2Channel> attachment) {
            if (result.intValue() < 0) {
                failed((Throwable) new EOFException(), attachment);
            } else {
                SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.OPEN_READ, false);
            }
        }

        @Override // java.nio.channels.CompletionHandler
        public void failed(Throwable exc, SocketWrapperBase<Nio2Channel> attachment) {
            SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.ERROR, false);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel$HandshakeWriteCompletionHandler.class */
    private class HandshakeWriteCompletionHandler implements CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> {
        private HandshakeWriteCompletionHandler() {
        }

        @Override // java.nio.channels.CompletionHandler
        public void completed(Integer result, SocketWrapperBase<Nio2Channel> attachment) {
            if (result.intValue() < 0) {
                failed((Throwable) new EOFException(), attachment);
            } else {
                SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.OPEN_WRITE, false);
            }
        }

        @Override // java.nio.channels.CompletionHandler
        public void failed(Throwable exc, SocketWrapperBase<Nio2Channel> attachment) {
            SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.ERROR, false);
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public void reset(AsynchronousSocketChannel channel, SocketWrapperBase<Nio2Channel> socket) throws IOException {
        super.reset(channel, socket);
        this.sslEngine = null;
        this.sniComplete = false;
        this.handshakeComplete = false;
        this.unwrapBeforeRead = true;
        this.closed = false;
        this.closing = false;
        this.netInBuffer.clear();
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public void free() {
        super.free();
        if (this.endpoint.getSocketProperties().getDirectSslBuffer()) {
            ByteBufferUtils.cleanDirectBuffer(this.netInBuffer);
            ByteBufferUtils.cleanDirectBuffer(this.netOutBuffer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel$FutureFlush.class */
    public class FutureFlush implements Future<Boolean> {
        private Future<Integer> integer;
        private Exception e;

        protected FutureFlush() {
            this.e = null;
            try {
                this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer);
            } catch (IllegalStateException e) {
                this.e = e;
            }
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (this.e != null) {
                return true;
            }
            return this.integer.cancel(mayInterruptIfRunning);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            if (this.e != null) {
                return true;
            }
            return this.integer.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            if (this.e != null) {
                return true;
            }
            return this.integer.isDone();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Boolean get() throws InterruptedException, ExecutionException {
            if (this.e != null) {
                throw new ExecutionException(this.e);
            }
            return Boolean.valueOf(this.integer.get().intValue() >= 0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (this.e != null) {
                throw new ExecutionException(this.e);
            }
            return Boolean.valueOf(this.integer.get(timeout, unit).intValue() >= 0);
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public Future<Boolean> flush() {
        return new FutureFlush();
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public int handshake() throws IOException {
        return handshakeInternal(true);
    }

    protected int handshakeInternal(boolean async) throws IOException {
        int read;
        SSLEngineResult handshake;
        if (this.handshakeComplete) {
            return 0;
        }
        if (!this.sniComplete) {
            int sniResult = processSNI();
            if (sniResult == 0) {
                this.sniComplete = true;
            } else {
                return sniResult;
            }
        }
        long timeout = this.endpoint.getConnectionTimeout();
        while (!this.handshakeComplete) {
            switch (AnonymousClass5.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[this.handshakeStatus.ordinal()]) {
                case 1:
                    throw new IOException(sm.getString("channel.nio.ssl.notHandshaking"));
                case 2:
                    if (this.endpoint.hasNegotiableProtocols()) {
                        if (this.sslEngine instanceof SSLUtil.ProtocolInfo) {
                            this.socketWrapper.setNegotiatedProtocol(((SSLUtil.ProtocolInfo) this.sslEngine).getNegotiatedProtocol());
                        } else if (JreCompat.isJre9Available()) {
                            this.socketWrapper.setNegotiatedProtocol(JreCompat.getInstance().getApplicationProtocol(this.sslEngine));
                        }
                    }
                    this.handshakeComplete = !this.netOutBuffer.hasRemaining();
                    if (this.handshakeComplete) {
                        return 0;
                    }
                    if (async) {
                        this.sc.write(this.netOutBuffer, AbstractEndpoint.toTimeout(timeout), TimeUnit.MILLISECONDS, this.socketWrapper, this.handshakeWriteCompletionHandler);
                        return 1;
                    }
                    try {
                        if (timeout > 0) {
                            this.sc.write(this.netOutBuffer).get(timeout, TimeUnit.MILLISECONDS);
                        } else {
                            this.sc.write(this.netOutBuffer).get();
                        }
                        return 1;
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        throw new IOException(sm.getString("channel.nio.ssl.handshakeError"));
                    }
                case 3:
                    try {
                        handshake = handshakeWrap();
                    } catch (SSLException e2) {
                        if (log.isDebugEnabled()) {
                            log.debug(sm.getString("channel.nio.ssl.wrapException"), e2);
                        }
                        handshake = handshakeWrap();
                    }
                    if (handshake.getStatus() == SSLEngineResult.Status.OK) {
                        if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                            this.handshakeStatus = tasks();
                        }
                        if (this.handshakeStatus != SSLEngineResult.HandshakeStatus.NEED_UNWRAP || this.netOutBuffer.remaining() > 0) {
                            if (async) {
                                this.sc.write(this.netOutBuffer, AbstractEndpoint.toTimeout(timeout), TimeUnit.MILLISECONDS, this.socketWrapper, this.handshakeWriteCompletionHandler);
                                return 1;
                            }
                            try {
                                if (timeout > 0) {
                                    this.sc.write(this.netOutBuffer).get(timeout, TimeUnit.MILLISECONDS);
                                } else {
                                    this.sc.write(this.netOutBuffer).get();
                                }
                                return 1;
                            } catch (InterruptedException | ExecutionException | TimeoutException e3) {
                                throw new IOException(sm.getString("channel.nio.ssl.handshakeError"));
                            }
                        }
                    } else if (handshake.getStatus() == SSLEngineResult.Status.CLOSED) {
                        return -1;
                    } else {
                        throw new IOException(sm.getString("channel.nio.ssl.unexpectedStatusDuringWrap", handshake.getStatus()));
                    }
                    break;
                case 4:
                    break;
                case 5:
                    this.handshakeStatus = tasks();
                    continue;
                default:
                    throw new IllegalStateException(sm.getString("channel.nio.ssl.invalidStatus", this.handshakeStatus));
            }
            SSLEngineResult handshake2 = handshakeUnwrap();
            if (handshake2.getStatus() == SSLEngineResult.Status.OK) {
                if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                    this.handshakeStatus = tasks();
                }
            } else if (handshake2.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                if (this.netInBuffer.position() == this.netInBuffer.limit()) {
                    this.netInBuffer.clear();
                }
                if (async) {
                    this.sc.read(this.netInBuffer, AbstractEndpoint.toTimeout(timeout), TimeUnit.MILLISECONDS, this.socketWrapper, this.handshakeReadCompletionHandler);
                    return 1;
                }
                try {
                    if (timeout > 0) {
                        read = this.sc.read(this.netInBuffer).get(timeout, TimeUnit.MILLISECONDS).intValue();
                    } else {
                        read = this.sc.read(this.netInBuffer).get().intValue();
                    }
                    if (read == -1) {
                        throw new EOFException();
                    }
                    return 1;
                } catch (InterruptedException | ExecutionException | TimeoutException e4) {
                    throw new IOException(sm.getString("channel.nio.ssl.handshakeError"));
                }
            } else {
                throw new IOException(sm.getString("channel.nio.ssl.unexpectedStatusDuringUnwrap", handshake2.getStatus()));
            }
        }
        if (this.handshakeComplete) {
            return 0;
        }
        return handshakeInternal(async);
    }

    private int processSNI() throws IOException {
        if (this.netInBuffer.position() == 0) {
            this.sc.read(this.netInBuffer, AbstractEndpoint.toTimeout(this.endpoint.getConnectionTimeout()), TimeUnit.MILLISECONDS, this.socketWrapper, this.handshakeReadCompletionHandler);
            return 1;
        }
        TLSClientHelloExtractor extractor = new TLSClientHelloExtractor(this.netInBuffer);
        if (extractor.getResult() == TLSClientHelloExtractor.ExtractorResult.UNDERFLOW && this.netInBuffer.capacity() < this.endpoint.getSniParseLimit()) {
            int newLimit = Math.min(this.netInBuffer.capacity() * 2, this.endpoint.getSniParseLimit());
            log.info(sm.getString("channel.nio.ssl.expandNetInBuffer", Integer.toString(newLimit)));
            this.netInBuffer = ByteBufferUtils.expand(this.netInBuffer, newLimit);
            this.sc.read(this.netInBuffer, AbstractEndpoint.toTimeout(this.endpoint.getConnectionTimeout()), TimeUnit.MILLISECONDS, this.socketWrapper, this.handshakeReadCompletionHandler);
            return 1;
        }
        String hostName = null;
        List<Cipher> clientRequestedCiphers = null;
        List<String> clientRequestedApplicationProtocols = null;
        switch (extractor.getResult()) {
            case COMPLETE:
                hostName = extractor.getSNIValue();
                clientRequestedApplicationProtocols = extractor.getClientRequestedApplicationProtocols();
            case NOT_PRESENT:
                clientRequestedCiphers = extractor.getClientRequestedCiphers();
                break;
            case NEED_READ:
                this.sc.read(this.netInBuffer, AbstractEndpoint.toTimeout(this.endpoint.getConnectionTimeout()), TimeUnit.MILLISECONDS, this.socketWrapper, this.handshakeReadCompletionHandler);
                return 1;
            case UNDERFLOW:
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("channel.nio.ssl.sniDefault"));
                }
                hostName = this.endpoint.getDefaultSSLHostConfigName();
                clientRequestedCiphers = Collections.emptyList();
                break;
            case NON_SECURE:
                this.netOutBuffer.clear();
                this.netOutBuffer.put(TLSClientHelloExtractor.USE_TLS_RESPONSE);
                this.netOutBuffer.flip();
                flush();
                throw new IOException(sm.getString("channel.nio.ssl.foundHttp"));
        }
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("channel.nio.ssl.sniHostName", this.sc, hostName));
        }
        this.sslEngine = this.endpoint.createSSLEngine(hostName, clientRequestedCiphers, clientRequestedApplicationProtocols);
        getBufHandler().expand(this.sslEngine.getSession().getApplicationBufferSize());
        if (this.netOutBuffer.capacity() < this.sslEngine.getSession().getApplicationBufferSize()) {
            log.info(sm.getString("channel.nio.ssl.expandNetOutBuffer", Integer.toString(this.sslEngine.getSession().getApplicationBufferSize())));
        }
        this.netInBuffer = ByteBufferUtils.expand(this.netInBuffer, this.sslEngine.getSession().getPacketBufferSize());
        this.netOutBuffer = ByteBufferUtils.expand(this.netOutBuffer, this.sslEngine.getSession().getPacketBufferSize());
        this.netOutBuffer.position(0);
        this.netOutBuffer.limit(0);
        this.sslEngine.beginHandshake();
        this.handshakeStatus = this.sslEngine.getHandshakeStatus();
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.apache.tomcat.util.net.SecureNio2Channel$5  reason: invalid class name */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel$5.class */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus;

        static {
            try {
                $SwitchMap$org$apache$tomcat$util$net$TLSClientHelloExtractor$ExtractorResult[TLSClientHelloExtractor.ExtractorResult.COMPLETE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$tomcat$util$net$TLSClientHelloExtractor$ExtractorResult[TLSClientHelloExtractor.ExtractorResult.NOT_PRESENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$tomcat$util$net$TLSClientHelloExtractor$ExtractorResult[TLSClientHelloExtractor.ExtractorResult.NEED_READ.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$tomcat$util$net$TLSClientHelloExtractor$ExtractorResult[TLSClientHelloExtractor.ExtractorResult.UNDERFLOW.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$tomcat$util$net$TLSClientHelloExtractor$ExtractorResult[TLSClientHelloExtractor.ExtractorResult.NON_SECURE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus = new int[SSLEngineResult.HandshakeStatus.values().length];
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.FINISHED.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_WRAP.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_UNWRAP.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_TASK.ordinal()] = 5;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    public void rehandshake() throws IOException {
        if (this.netInBuffer.position() > 0 && this.netInBuffer.position() < this.netInBuffer.limit()) {
            throw new IOException(sm.getString("channel.nio.ssl.netInputNotEmpty"));
        }
        if (this.netOutBuffer.position() > 0 && this.netOutBuffer.position() < this.netOutBuffer.limit()) {
            throw new IOException(sm.getString("channel.nio.ssl.netOutputNotEmpty"));
        }
        if (!getBufHandler().isReadBufferEmpty()) {
            throw new IOException(sm.getString("channel.nio.ssl.appInputNotEmpty"));
        }
        if (!getBufHandler().isWriteBufferEmpty()) {
            throw new IOException(sm.getString("channel.nio.ssl.appOutputNotEmpty"));
        }
        this.netOutBuffer.position(0);
        this.netOutBuffer.limit(0);
        this.netInBuffer.position(0);
        this.netInBuffer.limit(0);
        getBufHandler().reset();
        this.handshakeComplete = false;
        this.sslEngine.beginHandshake();
        this.handshakeStatus = this.sslEngine.getHandshakeStatus();
        boolean handshaking = true;
        while (handshaking) {
            try {
                int hsStatus = handshakeInternal(false);
                switch (hsStatus) {
                    case -1:
                        throw new EOFException(sm.getString("channel.nio.ssl.eofDuringHandshake"));
                    case 0:
                        handshaking = false;
                        break;
                }
            } catch (IOException x) {
                closeSilently();
                throw x;
            } catch (Exception cx) {
                closeSilently();
                IOException x2 = new IOException(cx);
                throw x2;
            }
        }
    }

    protected SSLEngineResult.HandshakeStatus tasks() {
        while (true) {
            Runnable r = this.sslEngine.getDelegatedTask();
            if (r != null) {
                r.run();
            } else {
                return this.sslEngine.getHandshakeStatus();
            }
        }
    }

    protected SSLEngineResult handshakeWrap() throws IOException {
        this.netOutBuffer.clear();
        getBufHandler().configureWriteBufferForRead();
        SSLEngineResult result = this.sslEngine.wrap(getBufHandler().getWriteBuffer(), this.netOutBuffer);
        this.netOutBuffer.flip();
        this.handshakeStatus = result.getHandshakeStatus();
        return result;
    }

    protected SSLEngineResult handshakeUnwrap() throws IOException {
        SSLEngineResult result;
        boolean cont;
        do {
            this.netInBuffer.flip();
            getBufHandler().configureReadBufferForWrite();
            result = this.sslEngine.unwrap(this.netInBuffer, getBufHandler().getReadBuffer());
            this.netInBuffer.compact();
            this.handshakeStatus = result.getHandshakeStatus();
            if (result.getStatus() == SSLEngineResult.Status.OK && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                this.handshakeStatus = tasks();
            }
            cont = result.getStatus() == SSLEngineResult.Status.OK && this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        } while (cont);
        return result;
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel, java.nio.channels.AsynchronousChannel, java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closing) {
            return;
        }
        this.closing = true;
        this.sslEngine.closeOutbound();
        long timeout = this.endpoint.getConnectionTimeout();
        try {
            if (timeout > 0) {
                if (!flush().get(timeout, TimeUnit.MILLISECONDS).booleanValue()) {
                    closeSilently();
                    throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"));
                }
            } else if (!flush().get().booleanValue()) {
                closeSilently();
                throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"));
            }
            this.netOutBuffer.clear();
            SSLEngineResult handshake = this.sslEngine.wrap(getEmptyBuf(), this.netOutBuffer);
            if (handshake.getStatus() != SSLEngineResult.Status.CLOSED) {
                throw new IOException(sm.getString("channel.nio.ssl.invalidCloseState"));
            }
            this.netOutBuffer.flip();
            try {
                if (timeout > 0) {
                    if (!flush().get(timeout, TimeUnit.MILLISECONDS).booleanValue()) {
                        closeSilently();
                        throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"));
                    }
                } else if (!flush().get().booleanValue()) {
                    closeSilently();
                    throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"));
                }
                this.closed = (this.netOutBuffer.hasRemaining() || handshake.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP) ? false : true;
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                closeSilently();
                throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"), e);
            } catch (WritePendingException e2) {
                closeSilently();
                throw new IOException(sm.getString("channel.nio.ssl.pendingWriteDuringClose"), e2);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e3) {
            closeSilently();
            throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"), e3);
        } catch (WritePendingException e4) {
            closeSilently();
            throw new IOException(sm.getString("channel.nio.ssl.pendingWriteDuringClose"), e4);
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public void close(boolean force) throws IOException {
        try {
            close();
            if (force || this.closed) {
                this.closed = true;
                this.sc.close();
            }
        } catch (Throwable th) {
            if (force || this.closed) {
                this.closed = true;
                this.sc.close();
            }
            throw th;
        }
    }

    private void closeSilently() {
        try {
            close(true);
        } catch (IOException ioe) {
            log.debug(sm.getString("channel.nio.ssl.closeSilentError"), ioe);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel$FutureRead.class */
    private class FutureRead implements Future<Integer> {
        private ByteBuffer dst;
        private Future<Integer> integer;

        private FutureRead(ByteBuffer dst) {
            this.dst = dst;
            if (SecureNio2Channel.this.unwrapBeforeRead || SecureNio2Channel.this.netInBuffer.position() > 0) {
                this.integer = null;
            } else {
                this.integer = SecureNio2Channel.this.sc.read(SecureNio2Channel.this.netInBuffer);
            }
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (this.integer == null) {
                return false;
            }
            return this.integer.cancel(mayInterruptIfRunning);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            if (this.integer == null) {
                return false;
            }
            return this.integer.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            if (this.integer == null) {
                return true;
            }
            return this.integer.isDone();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Integer get() throws InterruptedException, ExecutionException {
            try {
                return this.integer == null ? unwrap(SecureNio2Channel.this.netInBuffer.position(), -1L, TimeUnit.MILLISECONDS) : unwrap(this.integer.get().intValue(), -1L, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                throw new ExecutionException(e);
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return this.integer == null ? unwrap(SecureNio2Channel.this.netInBuffer.position(), timeout, unit) : unwrap(this.integer.get(timeout, unit).intValue(), timeout, unit);
        }

        /* JADX WARN: Removed duplicated region for block: B:62:0x01d5 A[EDGE_INSN: B:62:0x01d5->B:52:0x01d5 ?: BREAK  , SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private java.lang.Integer unwrap(int r12, long r13, java.util.concurrent.TimeUnit r15) throws java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException, java.lang.InterruptedException {
            /*
                Method dump skipped, instructions count: 506
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.util.net.SecureNio2Channel.FutureRead.unwrap(int, long, java.util.concurrent.TimeUnit):java.lang.Integer");
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel, java.nio.channels.AsynchronousByteChannel
    public Future<Integer> read(ByteBuffer dst) {
        if (!this.handshakeComplete) {
            throw new IllegalStateException(sm.getString("channel.nio.ssl.incompleteHandshake"));
        }
        return new FutureRead(dst);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SecureNio2Channel$FutureWrite.class */
    private class FutureWrite implements Future<Integer> {
        private final ByteBuffer src;
        private Future<Integer> integer;
        private int written;
        private Throwable t;

        private FutureWrite(ByteBuffer src) {
            this.integer = null;
            this.written = 0;
            this.t = null;
            this.src = src;
            if (SecureNio2Channel.this.closing || SecureNio2Channel.this.closed) {
                this.t = new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.closing"));
            } else {
                wrap();
            }
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            return this.integer.cancel(mayInterruptIfRunning);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return this.integer.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return this.integer.isDone();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Integer get() throws InterruptedException, ExecutionException {
            if (this.t != null) {
                throw new ExecutionException(this.t);
            }
            if (this.integer.get().intValue() > 0 && this.written == 0) {
                wrap();
                return get();
            } else if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
                this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer);
                return get();
            } else {
                return Integer.valueOf(this.written);
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (this.t != null) {
                throw new ExecutionException(this.t);
            }
            if (this.integer.get(timeout, unit).intValue() > 0 && this.written == 0) {
                wrap();
                return get(timeout, unit);
            } else if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
                this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer);
                return get(timeout, unit);
            } else {
                return Integer.valueOf(this.written);
            }
        }

        protected void wrap() {
            try {
                if (!SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
                    SecureNio2Channel.this.netOutBuffer.clear();
                    SSLEngineResult result = SecureNio2Channel.this.sslEngine.wrap(this.src, SecureNio2Channel.this.netOutBuffer);
                    this.written = result.bytesConsumed();
                    SecureNio2Channel.this.netOutBuffer.flip();
                    if (result.getStatus() == SSLEngineResult.Status.OK) {
                        if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                            SecureNio2Channel.this.tasks();
                        }
                    } else {
                        this.t = new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.wrapFail", result.getStatus()));
                    }
                }
                this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer);
            } catch (SSLException e) {
                this.t = e;
            }
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel, java.nio.channels.AsynchronousByteChannel
    public Future<Integer> write(ByteBuffer src) {
        return new FutureWrite(src);
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public <A> void read(final ByteBuffer dst, final long timeout, final TimeUnit unit, final A attachment, final CompletionHandler<Integer, ? super A> handler) {
        if (this.closing || this.closed) {
            handler.completed(-1, attachment);
        } else if (!this.handshakeComplete) {
            throw new IllegalStateException(sm.getString("channel.nio.ssl.incompleteHandshake"));
        } else {
            CompletionHandler<Integer, A> readCompletionHandler = new CompletionHandler<Integer, A>() { // from class: org.apache.tomcat.util.net.SecureNio2Channel.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.nio.channels.CompletionHandler
                public /* bridge */ /* synthetic */ void completed(Integer num, Object obj) {
                    completed2(num, (Integer) obj);
                }

                /* JADX WARN: Code restructure failed: missing block: B:39:0x0180, code lost:
                    if (r12.hasRemaining() != false) goto L27;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:40:0x0183, code lost:
                    r9.this$0.unwrapBeforeRead = true;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:41:0x018f, code lost:
                    r9.this$0.unwrapBeforeRead = false;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:42:0x0198, code lost:
                    r11.completed(java.lang.Integer.valueOf(r13), r11);
                 */
                /* JADX WARN: Code restructure failed: missing block: B:54:?, code lost:
                    return;
                 */
                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Removed duplicated region for block: B:51:0x017c A[EDGE_INSN: B:51:0x017c->B:38:0x017c ?: BREAK  , SYNTHETIC] */
                /* renamed from: completed  reason: avoid collision after fix types in other method */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void completed2(java.lang.Integer r10, A r11) {
                    /*
                        Method dump skipped, instructions count: 434
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.util.net.SecureNio2Channel.AnonymousClass1.completed2(java.lang.Integer, java.lang.Object):void");
                }

                @Override // java.nio.channels.CompletionHandler
                public void failed(Throwable exc, A attach) {
                    handler.failed(exc, attach);
                }
            };
            if (this.unwrapBeforeRead || this.netInBuffer.position() > 0) {
                readCompletionHandler.completed(Integer.valueOf(this.netInBuffer.position()), attachment);
            } else {
                this.sc.read(this.netInBuffer, timeout, unit, attachment, readCompletionHandler);
            }
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public <A> void read(final ByteBuffer[] dsts, final int offset, final int length, final long timeout, final TimeUnit unit, final A attachment, final CompletionHandler<Long, ? super A> handler) {
        if (offset < 0 || dsts == null || offset + length > dsts.length) {
            throw new IllegalArgumentException();
        }
        if (this.closing || this.closed) {
            handler.completed(-1L, attachment);
        } else if (!this.handshakeComplete) {
            throw new IllegalStateException(sm.getString("channel.nio.ssl.incompleteHandshake"));
        } else {
            CompletionHandler<Integer, A> readCompletionHandler = new CompletionHandler<Integer, A>() { // from class: org.apache.tomcat.util.net.SecureNio2Channel.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.nio.channels.CompletionHandler
                public /* bridge */ /* synthetic */ void completed(Integer num, Object obj) {
                    completed2(num, (Integer) obj);
                }

                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Removed duplicated region for block: B:73:0x02cb A[Catch: Exception -> 0x0336, TryCatch #0 {Exception -> 0x0336, blocks: (B:5:0x0016, B:6:0x0029, B:8:0x0031, B:9:0x0036, B:11:0x0072, B:25:0x00e2, B:77:0x02db, B:80:0x02f6, B:83:0x0310, B:85:0x0325, B:84:0x031c, B:30:0x00f6, B:32:0x0101, B:35:0x011d, B:37:0x0136, B:40:0x016b, B:49:0x0200, B:41:0x0187, B:43:0x0191, B:45:0x01ac, B:48:0x01e5, B:54:0x0210, B:55:0x022b, B:71:0x02be, B:73:0x02cb, B:75:0x02d3, B:56:0x022c, B:57:0x023e, B:59:0x024a, B:61:0x0258, B:63:0x0274, B:62:0x0265, B:64:0x027a, B:66:0x0294, B:69:0x02a2, B:70:0x02bd, B:13:0x007d, B:15:0x008e, B:16:0x009f, B:18:0x00aa, B:19:0x00b2, B:23:0x00c3), top: B:89:0x0016 }] */
                /* JADX WARN: Removed duplicated region for block: B:93:0x02db A[EDGE_INSN: B:93:0x02db->B:77:0x02db ?: BREAK  , SYNTHETIC] */
                /* renamed from: completed  reason: avoid collision after fix types in other method */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void completed2(java.lang.Integer r10, A r11) {
                    /*
                        Method dump skipped, instructions count: 830
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.util.net.SecureNio2Channel.AnonymousClass2.completed2(java.lang.Integer, java.lang.Object):void");
                }

                @Override // java.nio.channels.CompletionHandler
                public void failed(Throwable exc, A attach) {
                    handler.failed(exc, attach);
                }
            };
            if (this.unwrapBeforeRead || this.netInBuffer.position() > 0) {
                readCompletionHandler.completed(Integer.valueOf(this.netInBuffer.position()), attachment);
            } else {
                this.sc.read(this.netInBuffer, timeout, unit, attachment, readCompletionHandler);
            }
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public <A> void write(final ByteBuffer src, final long timeout, final TimeUnit unit, final A attachment, final CompletionHandler<Integer, ? super A> handler) {
        if (this.closing || this.closed) {
            handler.failed(new IOException(sm.getString("channel.nio.ssl.closing")), attachment);
            return;
        }
        try {
            this.netOutBuffer.clear();
            SSLEngineResult result = this.sslEngine.wrap(src, this.netOutBuffer);
            final int written = result.bytesConsumed();
            this.netOutBuffer.flip();
            if (result.getStatus() == SSLEngineResult.Status.OK) {
                if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                    tasks();
                }
                this.sc.write(this.netOutBuffer, timeout, unit, attachment, new CompletionHandler<Integer, A>() { // from class: org.apache.tomcat.util.net.SecureNio2Channel.3
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.nio.channels.CompletionHandler
                    public /* bridge */ /* synthetic */ void completed(Integer num, Object obj) {
                        completed2(num, (Integer) obj);
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    /* renamed from: completed  reason: avoid collision after fix types in other method */
                    public void completed2(Integer nBytes, A attach) {
                        if (nBytes.intValue() < 0) {
                            failed(new EOFException(), attach);
                        } else if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
                            SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer, timeout, unit, attachment, this);
                        } else if (written == 0) {
                            SecureNio2Channel.this.write(src, timeout, unit, attachment, handler);
                        } else {
                            handler.completed(Integer.valueOf(written), attach);
                        }
                    }

                    @Override // java.nio.channels.CompletionHandler
                    public void failed(Throwable exc, A attach) {
                        handler.failed(exc, attach);
                    }
                });
                return;
            }
            throw new IOException(sm.getString("channel.nio.ssl.wrapFail", result.getStatus()));
        } catch (Exception e) {
            handler.failed(e, attachment);
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public <A> void write(final ByteBuffer[] srcs, final int offset, final int length, final long timeout, final TimeUnit unit, final A attachment, final CompletionHandler<Long, ? super A> handler) {
        if (offset < 0 || length < 0 || offset > srcs.length - length) {
            throw new IndexOutOfBoundsException();
        }
        if (this.closing || this.closed) {
            handler.failed(new IOException(sm.getString("channel.nio.ssl.closing")), attachment);
            return;
        }
        try {
            this.netOutBuffer.clear();
            SSLEngineResult result = this.sslEngine.wrap(srcs, offset, length, this.netOutBuffer);
            final int written = result.bytesConsumed();
            this.netOutBuffer.flip();
            if (result.getStatus() == SSLEngineResult.Status.OK) {
                if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                    tasks();
                }
                this.sc.write(this.netOutBuffer, timeout, unit, attachment, new CompletionHandler<Integer, A>() { // from class: org.apache.tomcat.util.net.SecureNio2Channel.4
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.nio.channels.CompletionHandler
                    public /* bridge */ /* synthetic */ void completed(Integer num, Object obj) {
                        completed2(num, (Integer) obj);
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    /* renamed from: completed  reason: avoid collision after fix types in other method */
                    public void completed2(Integer nBytes, A attach) {
                        if (nBytes.intValue() < 0) {
                            failed(new EOFException(), attach);
                        } else if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
                            SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer, timeout, unit, attachment, this);
                        } else if (written == 0) {
                            SecureNio2Channel.this.write(srcs, offset, length, timeout, unit, attachment, handler);
                        } else {
                            handler.completed(Long.valueOf(written), attach);
                        }
                    }

                    @Override // java.nio.channels.CompletionHandler
                    public void failed(Throwable exc, A attach) {
                        handler.failed(exc, attach);
                    }
                });
                return;
            }
            throw new IOException(sm.getString("channel.nio.ssl.wrapFail", result.getStatus()));
        } catch (Exception e) {
            handler.failed(e, attachment);
        }
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public boolean isHandshakeComplete() {
        return this.handshakeComplete;
    }

    @Override // org.apache.tomcat.util.net.Nio2Channel
    public boolean isClosing() {
        return this.closing;
    }

    public SSLEngine getSslEngine() {
        return this.sslEngine;
    }

    public ByteBuffer getEmptyBuf() {
        return emptyBuf;
    }
}
