package org.springframework.core.io.buffer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/buffer/PooledDataBuffer.class */
public interface PooledDataBuffer extends DataBuffer {
    boolean isAllocated();

    PooledDataBuffer retain();

    boolean release();
}
