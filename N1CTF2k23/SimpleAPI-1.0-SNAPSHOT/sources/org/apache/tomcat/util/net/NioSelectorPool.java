package org.apache.tomcat.util.net;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.util.backoff.ExponentialBackOff;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/NioSelectorPool.class */
public class NioSelectorPool {
    protected NioBlockingSelector blockingSelector;
    protected volatile Selector sharedSelector;
    protected boolean shared = Boolean.parseBoolean(System.getProperty("org.apache.tomcat.util.net.NioSelectorShared", "true"));
    protected int maxSelectors = 200;
    protected long sharedSelectorTimeout = ExponentialBackOff.DEFAULT_MAX_INTERVAL;
    protected int maxSpareSelectors = -1;
    protected boolean enabled = true;
    protected AtomicInteger active = new AtomicInteger(0);
    protected AtomicInteger spare = new AtomicInteger(0);
    protected ConcurrentLinkedQueue<Selector> selectors = new ConcurrentLinkedQueue<>();

    protected Selector getSharedSelector() throws IOException {
        if (this.shared && this.sharedSelector == null) {
            synchronized (NioSelectorPool.class) {
                if (this.sharedSelector == null) {
                    this.sharedSelector = Selector.open();
                }
            }
        }
        return this.sharedSelector;
    }

    public Selector get() throws IOException {
        if (this.shared) {
            return getSharedSelector();
        }
        if (!this.enabled || this.active.incrementAndGet() >= this.maxSelectors) {
            if (this.enabled) {
                this.active.decrementAndGet();
                return null;
            }
            return null;
        }
        Selector s = null;
        try {
            try {
                s = this.selectors.size() > 0 ? this.selectors.poll() : null;
                if (s == null) {
                    s = Selector.open();
                } else {
                    this.spare.decrementAndGet();
                }
                s = s;
            } catch (NoSuchElementException e) {
                try {
                    s = Selector.open();
                } catch (IOException e2) {
                }
                if (s == null) {
                    this.active.decrementAndGet();
                }
            }
            return s;
        } finally {
            if (0 == 0) {
                this.active.decrementAndGet();
            }
        }
    }

    public void put(Selector s) throws IOException {
        if (this.shared) {
            return;
        }
        if (this.enabled) {
            this.active.decrementAndGet();
        }
        if (this.enabled && (this.maxSpareSelectors == -1 || this.spare.get() < Math.min(this.maxSpareSelectors, this.maxSelectors))) {
            this.spare.incrementAndGet();
            this.selectors.offer(s);
            return;
        }
        s.close();
    }

    public void close() throws IOException {
        this.enabled = false;
        while (true) {
            Selector s = this.selectors.poll();
            if (s == null) {
                break;
            }
            s.close();
        }
        this.spare.set(0);
        this.active.set(0);
        if (this.blockingSelector != null) {
            this.blockingSelector.close();
        }
        if (this.shared && getSharedSelector() != null) {
            getSharedSelector().close();
            this.sharedSelector = null;
        }
    }

    public void open(String name) throws IOException {
        this.enabled = true;
        getSharedSelector();
        if (this.shared) {
            this.blockingSelector = new NioBlockingSelector();
            this.blockingSelector.open(name, getSharedSelector());
        }
    }

    public int write(ByteBuffer buf, NioChannel socket, Selector selector, long writeTimeout) throws IOException {
        if (this.shared) {
            return this.blockingSelector.write(buf, socket, writeTimeout);
        }
        SelectionKey key = null;
        int written = 0;
        boolean timedout = false;
        int keycount = 1;
        long time = System.currentTimeMillis();
        while (!timedout) {
            try {
                if (!buf.hasRemaining()) {
                    break;
                }
                if (keycount > 0) {
                    int cnt = socket.write(buf);
                    if (cnt == -1) {
                        throw new EOFException();
                    }
                    written += cnt;
                    if (cnt > 0) {
                        time = System.currentTimeMillis();
                    }
                }
                if (selector != null) {
                    if (key == null) {
                        key = socket.getIOChannel().register(selector, 4);
                    } else {
                        key.interestOps(4);
                    }
                    if (writeTimeout == 0) {
                        timedout = buf.hasRemaining();
                    } else if (writeTimeout < 0) {
                        keycount = selector.select();
                    } else {
                        keycount = selector.select(writeTimeout);
                    }
                }
                if (writeTimeout > 0 && (selector == null || keycount == 0)) {
                    timedout = System.currentTimeMillis() - time >= writeTimeout;
                }
            } finally {
                if (key != null) {
                    key.cancel();
                    if (selector != null) {
                        selector.selectNow();
                    }
                }
            }
        }
        if (timedout) {
            throw new SocketTimeoutException();
        }
        return written;
    }

    public int read(ByteBuffer buf, NioChannel socket, Selector selector, long readTimeout) throws IOException {
        if (this.shared) {
            return this.blockingSelector.read(buf, socket, readTimeout);
        }
        SelectionKey key = null;
        int read = 0;
        boolean timedout = false;
        int keycount = 1;
        long time = System.currentTimeMillis();
        while (true) {
            if (timedout) {
                break;
            }
            if (keycount > 0) {
                try {
                    int cnt = socket.read(buf);
                    if (cnt == -1) {
                        if (read == 0) {
                            read = -1;
                        }
                    } else {
                        read += cnt;
                        if (cnt <= 0) {
                            if (cnt == 0 && read > 0) {
                                break;
                            }
                        }
                    }
                } finally {
                    if (key != null) {
                        key.cancel();
                        if (selector != null) {
                            selector.selectNow();
                        }
                    }
                }
            }
            if (selector != null) {
                if (key == null) {
                    key = socket.getIOChannel().register(selector, 1);
                } else {
                    key.interestOps(1);
                }
                if (readTimeout == 0) {
                    timedout = read == 0;
                } else if (readTimeout < 0) {
                    keycount = selector.select();
                } else {
                    keycount = selector.select(readTimeout);
                }
            }
            if (readTimeout > 0 && (selector == null || keycount == 0)) {
                timedout = System.currentTimeMillis() - time >= readTimeout;
            }
        }
        if (timedout) {
            throw new SocketTimeoutException();
        }
        return read;
    }

    public void setMaxSelectors(int maxSelectors) {
        this.maxSelectors = maxSelectors;
    }

    public void setMaxSpareSelectors(int maxSpareSelectors) {
        this.maxSpareSelectors = maxSpareSelectors;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSharedSelectorTimeout(long sharedSelectorTimeout) {
        this.sharedSelectorTimeout = sharedSelectorTimeout;
    }

    public int getMaxSelectors() {
        return this.maxSelectors;
    }

    public int getMaxSpareSelectors() {
        return this.maxSpareSelectors;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public long getSharedSelectorTimeout() {
        return this.sharedSelectorTimeout;
    }

    public ConcurrentLinkedQueue<Selector> getSelectors() {
        return this.selectors;
    }

    public AtomicInteger getSpare() {
        return this.spare;
    }

    public boolean isShared() {
        return this.shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
