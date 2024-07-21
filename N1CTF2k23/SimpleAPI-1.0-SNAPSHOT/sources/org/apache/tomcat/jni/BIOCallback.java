package org.apache.tomcat.jni;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/jni/BIOCallback.class */
public interface BIOCallback {
    int write(byte[] bArr);

    int read(byte[] bArr);

    int puts(String str);

    String gets(int i);
}
