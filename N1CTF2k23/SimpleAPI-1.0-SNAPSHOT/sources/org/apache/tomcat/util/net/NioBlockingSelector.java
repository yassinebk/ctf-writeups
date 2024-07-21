package org.apache.tomcat.util.net;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.collections.SynchronizedQueue;
import org.apache.tomcat.util.collections.SynchronizedStack;
import org.apache.tomcat.util.net.NioEndpoint;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioBlockingSelector.class */
public class NioBlockingSelector {
    private static final Log log = LogFactory.getLog(NioBlockingSelector.class);
    protected static final StringManager sm = StringManager.getManager(NioBlockingSelector.class);
    private final SynchronizedStack<KeyReference> keyReferenceStack = new SynchronizedStack<>();
    protected Selector sharedSelector;
    protected BlockPoller poller;

    public void open(String name, Selector selector) {
        this.sharedSelector = selector;
        this.poller = new BlockPoller();
        this.poller.selector = this.sharedSelector;
        this.poller.setDaemon(true);
        this.poller.setName(name + "-BlockPoller");
        this.poller.start();
    }

    public void close() {
        if (this.poller != null) {
            this.poller.disable();
            this.poller.interrupt();
            this.poller = null;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:15|(2:17|(3:25|26|27)(2:19|(4:21|22|23|24)))|28|29|(1:54)|33|34|(1:53)(1:38)|39|(5:44|45|(1:47)(1:50)|48|49)|24) */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int write(java.nio.ByteBuffer r6, org.apache.tomcat.util.net.NioChannel r7, long r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 397
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.util.net.NioBlockingSelector.write(java.nio.ByteBuffer, org.apache.tomcat.util.net.NioChannel, long):int");
    }

    public int read(ByteBuffer buf, NioChannel socket, long readTimeout) throws IOException {
        SelectionKey key = socket.getIOChannel().keyFor(socket.getSocketWrapper().getPoller().getSelector());
        if (key == null) {
            throw new IOException(sm.getString("nioBlockingSelector.keyNotRegistered"));
        }
        KeyReference reference = this.keyReferenceStack.pop();
        if (reference == null) {
            reference = new KeyReference();
        }
        NioEndpoint.NioSocketWrapper att = (NioEndpoint.NioSocketWrapper) key.attachment();
        int read = 0;
        boolean timedout = false;
        int keycount = 1;
        long time = System.currentTimeMillis();
        while (!timedout) {
            if (keycount > 0) {
                try {
                    read = socket.read(buf);
                    if (read != 0) {
                        break;
                    }
                } finally {
                    this.poller.remove(att, 1);
                    if (timedout && reference.key != null) {
                        this.poller.cancelKey(reference.key);
                    }
                    reference.key = null;
                    this.keyReferenceStack.push(reference);
                }
            }
            try {
                if (att.getReadLatch() == null || att.getReadLatch().getCount() == 0) {
                    att.startReadLatch(1);
                }
                this.poller.add(att, 1, reference);
                att.awaitReadLatch(AbstractEndpoint.toTimeout(readTimeout), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            }
            if (att.getReadLatch() != null && att.getReadLatch().getCount() > 0) {
                keycount = 0;
            } else {
                keycount = 1;
                att.resetReadLatch();
            }
            if (readTimeout >= 0 && keycount == 0) {
                timedout = System.currentTimeMillis() - time >= readTimeout;
            }
        }
        if (timedout) {
            throw new SocketTimeoutException();
        }
        return read;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioBlockingSelector$BlockPoller.class */
    protected static class BlockPoller extends Thread {
        protected volatile boolean run = true;
        protected Selector selector = null;
        protected final SynchronizedQueue<Runnable> events = new SynchronizedQueue<>();
        protected final AtomicInteger wakeupCounter = new AtomicInteger(0);

        protected BlockPoller() {
        }

        public void disable() {
            this.run = false;
            this.selector.wakeup();
        }

        public void cancelKey(SelectionKey key) {
            Runnable r = new RunnableCancel(key);
            this.events.offer(r);
            wakeup();
        }

        public void wakeup() {
            if (this.wakeupCounter.addAndGet(1) == 0) {
                this.selector.wakeup();
            }
        }

        public void cancel(SelectionKey sk, NioEndpoint.NioSocketWrapper key, int ops) {
            if (sk != null) {
                sk.cancel();
                sk.attach(null);
                if (4 == (ops & 4)) {
                    countDown(key.getWriteLatch());
                }
                if (1 == (ops & 1)) {
                    countDown(key.getReadLatch());
                }
            }
        }

        public void add(NioEndpoint.NioSocketWrapper key, int ops, KeyReference ref) {
            if (key == null) {
                return;
            }
            NioChannel nch = key.getSocket();
            SocketChannel ch2 = nch.getIOChannel();
            if (ch2 == null) {
                return;
            }
            Runnable r = new RunnableAdd(ch2, key, ops, ref);
            this.events.offer(r);
            wakeup();
        }

        public void remove(NioEndpoint.NioSocketWrapper key, int ops) {
            if (key == null) {
                return;
            }
            NioChannel nch = key.getSocket();
            SocketChannel ch2 = nch.getIOChannel();
            if (ch2 == null) {
                return;
            }
            Runnable r = new RunnableRemove(ch2, key, ops);
            this.events.offer(r);
            wakeup();
        }

        public boolean events() {
            Runnable r;
            int size = this.events.size();
            for (int i = 0; i < size && (r = this.events.poll()) != null; i++) {
                r.run();
            }
            return size > 0;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int keyCount;
            while (this.run) {
                try {
                    events();
                    try {
                        int i = this.wakeupCounter.get();
                        if (i > 0) {
                            keyCount = this.selector.selectNow();
                        } else {
                            this.wakeupCounter.set(-1);
                            keyCount = this.selector.select(1000L);
                        }
                        this.wakeupCounter.set(0);
                    } catch (NullPointerException x) {
                        if (this.selector != null) {
                            if (NioBlockingSelector.log.isDebugEnabled()) {
                                NioBlockingSelector.log.debug("Possibly encountered sun bug 5076772 on windows JDK 1.5", x);
                            }
                        } else {
                            throw x;
                            break;
                        }
                    } catch (CancelledKeyException x2) {
                        if (NioBlockingSelector.log.isDebugEnabled()) {
                            NioBlockingSelector.log.debug("Possibly encountered sun bug 5076772 on windows JDK 1.5", x2);
                        }
                    } catch (Throwable x3) {
                        ExceptionUtils.handleThrowable(x3);
                        NioBlockingSelector.log.error(NioBlockingSelector.sm.getString("nioBlockingSelector.selectError"), x3);
                    }
                } catch (Throwable t) {
                    NioBlockingSelector.log.error(NioBlockingSelector.sm.getString("nioBlockingSelector.processingError"), t);
                }
                if (!this.run) {
                    break;
                }
                Iterator<SelectionKey> iterator = keyCount > 0 ? this.selector.selectedKeys().iterator() : null;
                while (this.run && iterator != null && iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    NioEndpoint.NioSocketWrapper socketWrapper = (NioEndpoint.NioSocketWrapper) sk.attachment();
                    try {
                        iterator.remove();
                        sk.interestOps(sk.interestOps() & (sk.readyOps() ^ (-1)));
                        if (sk.isReadable()) {
                            countDown(socketWrapper.getReadLatch());
                        }
                        if (sk.isWritable()) {
                            countDown(socketWrapper.getWriteLatch());
                        }
                    } catch (CancelledKeyException e) {
                        sk.cancel();
                        countDown(socketWrapper.getReadLatch());
                        countDown(socketWrapper.getWriteLatch());
                    }
                }
            }
            this.events.clear();
            if (this.selector.isOpen()) {
                try {
                    this.selector.selectNow();
                } catch (Exception ignore) {
                    if (NioBlockingSelector.log.isDebugEnabled()) {
                        NioBlockingSelector.log.debug("", ignore);
                    }
                }
            }
            try {
                this.selector.close();
            } catch (Exception ignore2) {
                if (NioBlockingSelector.log.isDebugEnabled()) {
                    NioBlockingSelector.log.debug("", ignore2);
                }
            }
        }

        public void countDown(CountDownLatch latch) {
            if (latch == null) {
                return;
            }
            latch.countDown();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioBlockingSelector$BlockPoller$RunnableAdd.class */
        public class RunnableAdd implements Runnable {

            /* renamed from: ch  reason: collision with root package name */
            private final SocketChannel f0ch;
            private final NioEndpoint.NioSocketWrapper key;
            private final int ops;
            private final KeyReference ref;

            public RunnableAdd(SocketChannel ch2, NioEndpoint.NioSocketWrapper key, int ops, KeyReference ref) {
                this.f0ch = ch2;
                this.key = key;
                this.ops = ops;
                this.ref = ref;
            }

            @Override // java.lang.Runnable
            public void run() {
                SelectionKey sk = this.f0ch.keyFor(BlockPoller.this.selector);
                try {
                    if (sk == null) {
                        sk = this.f0ch.register(BlockPoller.this.selector, this.ops, this.key);
                        this.ref.key = sk;
                    } else if (!sk.isValid()) {
                        BlockPoller.this.cancel(sk, this.key, this.ops);
                    } else {
                        sk.interestOps(sk.interestOps() | this.ops);
                    }
                } catch (CancelledKeyException e) {
                    BlockPoller.this.cancel(sk, this.key, this.ops);
                } catch (ClosedChannelException e2) {
                    BlockPoller.this.cancel(null, this.key, this.ops);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioBlockingSelector$BlockPoller$RunnableRemove.class */
        public class RunnableRemove implements Runnable {

            /* renamed from: ch  reason: collision with root package name */
            private final SocketChannel f1ch;
            private final NioEndpoint.NioSocketWrapper key;
            private final int ops;

            public RunnableRemove(SocketChannel ch2, NioEndpoint.NioSocketWrapper key, int ops) {
                this.f1ch = ch2;
                this.key = key;
                this.ops = ops;
            }

            @Override // java.lang.Runnable
            public void run() {
                SelectionKey sk = this.f1ch.keyFor(BlockPoller.this.selector);
                try {
                    if (sk == null) {
                        if (4 == (this.ops & 4)) {
                            BlockPoller.this.countDown(this.key.getWriteLatch());
                        }
                        if (1 == (this.ops & 1)) {
                            BlockPoller.this.countDown(this.key.getReadLatch());
                        }
                    } else if (sk.isValid()) {
                        sk.interestOps(sk.interestOps() & (this.ops ^ (-1)));
                        if (4 == (this.ops & 4)) {
                            BlockPoller.this.countDown(this.key.getWriteLatch());
                        }
                        if (1 == (this.ops & 1)) {
                            BlockPoller.this.countDown(this.key.getReadLatch());
                        }
                        if (sk.interestOps() == 0) {
                            sk.cancel();
                            sk.attach(null);
                        }
                    } else {
                        sk.cancel();
                        sk.attach(null);
                    }
                } catch (CancelledKeyException e) {
                    if (sk != null) {
                        sk.cancel();
                        sk.attach(null);
                    }
                }
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioBlockingSelector$BlockPoller$RunnableCancel.class */
        public static class RunnableCancel implements Runnable {
            private final SelectionKey key;

            public RunnableCancel(SelectionKey key) {
                this.key = key;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.key.cancel();
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioBlockingSelector$KeyReference.class */
    public static class KeyReference {
        SelectionKey key = null;

        protected void finalize() {
            if (this.key != null && this.key.isValid()) {
                NioBlockingSelector.log.warn(NioBlockingSelector.sm.getString("nioBlockingSelector.possibleLeak"));
                try {
                    this.key.cancel();
                } catch (Exception e) {
                }
            }
        }
    }
}
