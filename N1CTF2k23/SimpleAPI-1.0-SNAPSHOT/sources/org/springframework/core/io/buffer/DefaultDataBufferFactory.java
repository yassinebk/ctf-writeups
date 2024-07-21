package org.springframework.core.io.buffer;

import java.nio.ByteBuffer;
import java.util.List;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/DefaultDataBufferFactory.class */
public class DefaultDataBufferFactory implements DataBufferFactory {
    public static final int DEFAULT_INITIAL_CAPACITY = 256;
    private final boolean preferDirect;
    private final int defaultInitialCapacity;

    @Override // org.springframework.core.io.buffer.DataBufferFactory
    public /* bridge */ /* synthetic */ DataBuffer join(List list) {
        return join((List<? extends DataBuffer>) list);
    }

    public DefaultDataBufferFactory() {
        this(false);
    }

    public DefaultDataBufferFactory(boolean preferDirect) {
        this(preferDirect, 256);
    }

    public DefaultDataBufferFactory(boolean preferDirect, int defaultInitialCapacity) {
        Assert.isTrue(defaultInitialCapacity > 0, "'defaultInitialCapacity' should be larger than 0");
        this.preferDirect = preferDirect;
        this.defaultInitialCapacity = defaultInitialCapacity;
    }

    @Override // org.springframework.core.io.buffer.DataBufferFactory
    public DefaultDataBuffer allocateBuffer() {
        return allocateBuffer(this.defaultInitialCapacity);
    }

    @Override // org.springframework.core.io.buffer.DataBufferFactory
    public DefaultDataBuffer allocateBuffer(int initialCapacity) {
        ByteBuffer allocate;
        if (this.preferDirect) {
            allocate = ByteBuffer.allocateDirect(initialCapacity);
        } else {
            allocate = ByteBuffer.allocate(initialCapacity);
        }
        ByteBuffer byteBuffer = allocate;
        return DefaultDataBuffer.fromEmptyByteBuffer(this, byteBuffer);
    }

    @Override // org.springframework.core.io.buffer.DataBufferFactory
    public DefaultDataBuffer wrap(ByteBuffer byteBuffer) {
        return DefaultDataBuffer.fromFilledByteBuffer(this, byteBuffer.slice());
    }

    @Override // org.springframework.core.io.buffer.DataBufferFactory
    public DefaultDataBuffer wrap(byte[] bytes) {
        return DefaultDataBuffer.fromFilledByteBuffer(this, ByteBuffer.wrap(bytes));
    }

    @Override // org.springframework.core.io.buffer.DataBufferFactory
    public DefaultDataBuffer join(List<? extends DataBuffer> dataBuffers) {
        Assert.notEmpty(dataBuffers, "DataBuffer List must not be empty");
        int capacity = dataBuffers.stream().mapToInt((v0) -> {
            return v0.readableByteCount();
        }).sum();
        DefaultDataBuffer result = allocateBuffer(capacity);
        result.getClass();
        dataBuffers.forEach(xva$0 -> {
            result.write(xva$0);
        });
        dataBuffers.forEach(DataBufferUtils::release);
        return result;
    }

    public String toString() {
        return "DefaultDataBufferFactory (preferDirect=" + this.preferDirect + ")";
    }
}
