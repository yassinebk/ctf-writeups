package org.apache.tomcat.websocket.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.websocket.Transformation;
import org.apache.tomcat.websocket.WsRemoteEndpointImplBase;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/server/WsRemoteEndpointImplServer.class */
public class WsRemoteEndpointImplServer extends WsRemoteEndpointImplBase {
    private static final StringManager sm = StringManager.getManager(WsRemoteEndpointImplServer.class);
    private final SocketWrapperBase<?> socketWrapper;
    private final WsWriteTimeout wsWriteTimeout;
    private final Log log = LogFactory.getLog(WsRemoteEndpointImplServer.class);
    private volatile SendHandler handler = null;
    private volatile ByteBuffer[] buffers = null;
    private volatile long timeoutExpiry = -1;

    public WsRemoteEndpointImplServer(SocketWrapperBase<?> socketWrapper, WsServerContainer serverContainer) {
        this.socketWrapper = socketWrapper;
        this.wsWriteTimeout = serverContainer.getTimeout();
    }

    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    protected final boolean isMasked() {
        return false;
    }

    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    protected void doWrite(final SendHandler handler, final long blockingWriteTimeoutExpiry, ByteBuffer... buffers) {
        long timeout;
        if (this.socketWrapper.hasAsyncIO()) {
            final boolean block = blockingWriteTimeoutExpiry != -1;
            if (block) {
                timeout = blockingWriteTimeoutExpiry - System.currentTimeMillis();
                if (timeout <= 0) {
                    SendResult sr = new SendResult(new SocketTimeoutException());
                    handler.onResult(sr);
                    return;
                }
            } else {
                this.handler = handler;
                timeout = getSendTimeout();
                if (timeout > 0) {
                    this.timeoutExpiry = timeout + System.currentTimeMillis();
                    this.wsWriteTimeout.register(this);
                }
            }
            this.socketWrapper.write(block ? SocketWrapperBase.BlockingMode.BLOCK : SocketWrapperBase.BlockingMode.SEMI_BLOCK, timeout, TimeUnit.MILLISECONDS, null, SocketWrapperBase.COMPLETE_WRITE_WITH_COMPLETION, new CompletionHandler<Long, Void>() { // from class: org.apache.tomcat.websocket.server.WsRemoteEndpointImplServer.1
                @Override // java.nio.channels.CompletionHandler
                public void completed(Long result, Void attachment) {
                    if (!block) {
                        WsRemoteEndpointImplServer.this.wsWriteTimeout.unregister(WsRemoteEndpointImplServer.this);
                        WsRemoteEndpointImplServer.this.clearHandler(null, true);
                        return;
                    }
                    long timeout2 = blockingWriteTimeoutExpiry - System.currentTimeMillis();
                    if (timeout2 > 0) {
                        handler.onResult(WsRemoteEndpointImplServer.SENDRESULT_OK);
                    } else {
                        failed((Throwable) new SocketTimeoutException(), (Void) null);
                    }
                }

                @Override // java.nio.channels.CompletionHandler
                public void failed(Throwable exc, Void attachment) {
                    if (!block) {
                        WsRemoteEndpointImplServer.this.wsWriteTimeout.unregister(WsRemoteEndpointImplServer.this);
                        WsRemoteEndpointImplServer.this.clearHandler(exc, true);
                        WsRemoteEndpointImplServer.this.close();
                        return;
                    }
                    SendResult sr2 = new SendResult(exc);
                    handler.onResult(sr2);
                }
            }, buffers);
        } else if (blockingWriteTimeoutExpiry == -1) {
            this.handler = handler;
            this.buffers = buffers;
            onWritePossible(true);
        } else {
            try {
                for (ByteBuffer buffer : buffers) {
                    long timeout2 = blockingWriteTimeoutExpiry - System.currentTimeMillis();
                    if (timeout2 <= 0) {
                        SendResult sr2 = new SendResult(new SocketTimeoutException());
                        handler.onResult(sr2);
                        return;
                    }
                    this.socketWrapper.setWriteTimeout(timeout2);
                    this.socketWrapper.write(true, buffer);
                }
                long timeout3 = blockingWriteTimeoutExpiry - System.currentTimeMillis();
                if (timeout3 <= 0) {
                    SendResult sr3 = new SendResult(new SocketTimeoutException());
                    handler.onResult(sr3);
                    return;
                }
                this.socketWrapper.setWriteTimeout(timeout3);
                this.socketWrapper.flush(true);
                handler.onResult(SENDRESULT_OK);
            } catch (IOException e) {
                SendResult sr4 = new SendResult(e);
                handler.onResult(sr4);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x005b, code lost:
        r6.socketWrapper.flush(false);
        r9 = r6.socketWrapper.isReadyForWrite();
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006d, code lost:
        if (r9 == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0070, code lost:
        r6.wsWriteTimeout.unregister(r6);
        clearHandler(null, r7);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onWritePossible(boolean r7) {
        /*
            r6 = this;
            r0 = r6
            java.nio.ByteBuffer[] r0 = r0.buffers
            r8 = r0
            r0 = r8
            if (r0 != 0) goto La
            return
        La:
            r0 = 0
            r9 = r0
            r0 = r6
            org.apache.tomcat.util.net.SocketWrapperBase<?> r0 = r0.socketWrapper     // Catch: java.lang.Throwable -> L84
            r1 = 0
            boolean r0 = r0.flush(r1)     // Catch: java.lang.Throwable -> L84
        L15:
            r0 = r6
            org.apache.tomcat.util.net.SocketWrapperBase<?> r0 = r0.socketWrapper     // Catch: java.lang.Throwable -> L84
            boolean r0 = r0.isReadyForWrite()     // Catch: java.lang.Throwable -> L84
            if (r0 == 0) goto L81
            r0 = 1
            r9 = r0
            r0 = r8
            r10 = r0
            r0 = r10
            int r0 = r0.length     // Catch: java.lang.Throwable -> L84
            r11 = r0
            r0 = 0
            r12 = r0
        L2c:
            r0 = r12
            r1 = r11
            if (r0 >= r1) goto L57
            r0 = r10
            r1 = r12
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L84
            r13 = r0
            r0 = r13
            boolean r0 = r0.hasRemaining()     // Catch: java.lang.Throwable -> L84
            if (r0 == 0) goto L51
            r0 = 0
            r9 = r0
            r0 = r6
            org.apache.tomcat.util.net.SocketWrapperBase<?> r0 = r0.socketWrapper     // Catch: java.lang.Throwable -> L84
            r1 = 0
            r2 = r13
            r0.write(r1, r2)     // Catch: java.lang.Throwable -> L84
            goto L57
        L51:
            int r12 = r12 + 1
            goto L2c
        L57:
            r0 = r9
            if (r0 == 0) goto L15
            r0 = r6
            org.apache.tomcat.util.net.SocketWrapperBase<?> r0 = r0.socketWrapper     // Catch: java.lang.Throwable -> L84
            r1 = 0
            boolean r0 = r0.flush(r1)     // Catch: java.lang.Throwable -> L84
            r0 = r6
            org.apache.tomcat.util.net.SocketWrapperBase<?> r0 = r0.socketWrapper     // Catch: java.lang.Throwable -> L84
            boolean r0 = r0.isReadyForWrite()     // Catch: java.lang.Throwable -> L84
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L81
            r0 = r6
            org.apache.tomcat.websocket.server.WsWriteTimeout r0 = r0.wsWriteTimeout     // Catch: java.lang.Throwable -> L84
            r1 = r6
            r0.unregister(r1)     // Catch: java.lang.Throwable -> L84
            r0 = r6
            r1 = 0
            r2 = r7
            r0.clearHandler(r1, r2)     // Catch: java.lang.Throwable -> L84
            goto L81
        L81:
            goto L99
        L84:
            r10 = move-exception
            r0 = r6
            org.apache.tomcat.websocket.server.WsWriteTimeout r0 = r0.wsWriteTimeout
            r1 = r6
            r0.unregister(r1)
            r0 = r6
            r1 = r10
            r2 = r7
            r0.clearHandler(r1, r2)
            r0 = r6
            r0.close()
        L99:
            r0 = r9
            if (r0 != 0) goto Lbc
            r0 = r6
            long r0 = r0.getSendTimeout()
            r10 = r0
            r0 = r10
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto Lbc
            r0 = r6
            r1 = r10
            long r2 = java.lang.System.currentTimeMillis()
            long r1 = r1 + r2
            r0.timeoutExpiry = r1
            r0 = r6
            org.apache.tomcat.websocket.server.WsWriteTimeout r0 = r0.wsWriteTimeout
            r1 = r6
            r0.register(r1)
        Lbc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.websocket.server.WsRemoteEndpointImplServer.onWritePossible(boolean):void");
    }

    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    protected void doClose() {
        if (this.handler != null) {
            clearHandler(new EOFException(), true);
        }
        try {
            this.socketWrapper.close();
        } catch (Exception e) {
            if (this.log.isInfoEnabled()) {
                this.log.info(sm.getString("wsRemoteEndpointServer.closeFailed"), e);
            }
        }
        this.wsWriteTimeout.unregister(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getTimeoutExpiry() {
        return this.timeoutExpiry;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onTimeout(boolean useDispatch) {
        if (this.handler != null) {
            clearHandler(new SocketTimeoutException(), useDispatch);
        }
        close();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    public void setTransformation(Transformation transformation) {
        super.setTransformation(transformation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearHandler(Throwable t, boolean useDispatch) {
        SendHandler sh = this.handler;
        this.handler = null;
        this.buffers = null;
        if (sh != null) {
            if (useDispatch) {
                OnResultRunnable r = new OnResultRunnable(sh, t);
                try {
                    this.socketWrapper.execute(r);
                } catch (RejectedExecutionException e) {
                    r.run();
                }
            } else if (t == null) {
                sh.onResult(new SendResult());
            } else {
                sh.onResult(new SendResult(t));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/server/WsRemoteEndpointImplServer$OnResultRunnable.class */
    public static class OnResultRunnable implements Runnable {
        private final SendHandler sh;
        private final Throwable t;

        private OnResultRunnable(SendHandler sh, Throwable t) {
            this.sh = sh;
            this.t = t;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.t == null) {
                this.sh.onResult(new SendResult());
            } else {
                this.sh.onResult(new SendResult(this.t));
            }
        }
    }
}
