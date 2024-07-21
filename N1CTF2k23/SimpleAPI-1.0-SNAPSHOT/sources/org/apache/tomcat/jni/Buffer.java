package org.apache.tomcat.jni;

import java.nio.ByteBuffer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/jni/Buffer.class */
public class Buffer {
    public static native ByteBuffer malloc(int i);

    public static native ByteBuffer calloc(int i, int i2);

    public static native ByteBuffer palloc(long j, int i);

    public static native ByteBuffer pcalloc(long j, int i);

    public static native ByteBuffer create(long j, int i);

    public static native void free(ByteBuffer byteBuffer);

    public static native long address(ByteBuffer byteBuffer);

    public static native long size(ByteBuffer byteBuffer);
}
