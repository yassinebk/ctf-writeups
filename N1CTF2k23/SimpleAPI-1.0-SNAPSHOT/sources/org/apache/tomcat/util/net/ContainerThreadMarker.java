package org.apache.tomcat.util.net;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/ContainerThreadMarker.class */
public class ContainerThreadMarker {
    private static final ThreadLocal<Boolean> marker = new ThreadLocal<>();

    public static boolean isContainerThread() {
        Boolean flag = marker.get();
        if (flag == null) {
            return false;
        }
        return flag.booleanValue();
    }

    public static void set() {
        marker.set(Boolean.TRUE);
    }

    public static void clear() {
        marker.set(Boolean.FALSE);
    }
}
