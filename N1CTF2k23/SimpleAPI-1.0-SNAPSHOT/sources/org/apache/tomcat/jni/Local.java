package org.apache.tomcat.jni;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/jni/Local.class */
public class Local {
    public static native long create(String str, long j) throws Exception;

    public static native int bind(long j, long j2);

    public static native int listen(long j, int i);

    public static native long accept(long j) throws Exception;

    public static native int connect(long j, long j2);
}
