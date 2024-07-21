package org.apache.coyote.http2;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http2/AbstractStream.class */
abstract class AbstractStream {
    private static final Log log = LogFactory.getLog(AbstractStream.class);
    private static final StringManager sm = StringManager.getManager(AbstractStream.class);
    private final Integer identifier;
    private volatile AbstractStream parentStream = null;
    private final Set<Stream> childStreams = Collections.newSetFromMap(new ConcurrentHashMap());
    private long windowSize = 65535;

    abstract String getConnectionId();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getWeight();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractStream(Integer identifier) {
        this.identifier = identifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Integer getIdentifier() {
        return this.identifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int getIdAsInt() {
        return this.identifier.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void detachFromParent() {
        if (this.parentStream != null) {
            this.parentStream.getChildStreams().remove(this);
            this.parentStream = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void addChild(Stream child) {
        child.setParentStream(this);
        this.childStreams.add(child);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isDescendant(AbstractStream stream) {
        AbstractStream parent;
        AbstractStream parentStream = stream.getParentStream();
        while (true) {
            parent = parentStream;
            if (parent == null || parent == this) {
                break;
            }
            parentStream = parent.getParentStream();
        }
        return parent != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final AbstractStream getParentStream() {
        return this.parentStream;
    }

    final void setParentStream(AbstractStream parentStream) {
        this.parentStream = parentStream;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<Stream> getChildStreams() {
        return this.childStreams;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized long getWindowSize() {
        return this.windowSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void incrementWindowSize(int increment) throws Http2Exception {
        this.windowSize += increment;
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("abstractStream.windowSizeInc", getConnectionId(), getIdentifier(), Integer.toString(increment), Long.toString(this.windowSize)));
        }
        if (this.windowSize > 2147483647L) {
            String msg = sm.getString("abstractStream.windowSizeTooBig", getConnectionId(), this.identifier, Integer.toString(increment), Long.toString(this.windowSize));
            if (this.identifier.intValue() == 0) {
                throw new ConnectionException(msg, Http2Error.FLOW_CONTROL_ERROR);
            }
            throw new StreamException(msg, Http2Error.FLOW_CONTROL_ERROR, this.identifier.intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void decrementWindowSize(int decrement) {
        this.windowSize -= decrement;
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("abstractStream.windowSizeDec", getConnectionId(), getIdentifier(), Integer.toString(decrement), Long.toString(this.windowSize)));
        }
    }
}
