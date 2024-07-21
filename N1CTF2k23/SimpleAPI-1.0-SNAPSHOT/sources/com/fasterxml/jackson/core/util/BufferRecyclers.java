package com.fasterxml.jackson.core.util;

import java.lang.ref.SoftReference;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-core-2.11.0.jar:com/fasterxml/jackson/core/util/BufferRecyclers.class */
public class BufferRecyclers {
    public static final String SYSTEM_PROPERTY_TRACK_REUSABLE_BUFFERS = "com.fasterxml.jackson.core.util.BufferRecyclers.trackReusableBuffers";
    private static final ThreadLocalBufferManager _bufferRecyclerTracker;
    protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef;

    static {
        boolean trackReusableBuffers = false;
        try {
            trackReusableBuffers = "true".equals(System.getProperty(SYSTEM_PROPERTY_TRACK_REUSABLE_BUFFERS));
        } catch (SecurityException e) {
        }
        _bufferRecyclerTracker = trackReusableBuffers ? ThreadLocalBufferManager.instance() : null;
        _recyclerRef = new ThreadLocal<>();
    }

    public static BufferRecycler getBufferRecycler() {
        SoftReference<BufferRecycler> ref;
        SoftReference<BufferRecycler> ref2 = _recyclerRef.get();
        BufferRecycler br = ref2 == null ? null : ref2.get();
        if (br == null) {
            br = new BufferRecycler();
            if (_bufferRecyclerTracker != null) {
                ref = _bufferRecyclerTracker.wrapAndTrack(br);
            } else {
                ref = new SoftReference<>(br);
            }
            _recyclerRef.set(ref);
        }
        return br;
    }

    public static int releaseBuffers() {
        if (_bufferRecyclerTracker != null) {
            return _bufferRecyclerTracker.releaseBuffers();
        }
        return -1;
    }
}
