package org.apache.coyote.http2;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/http2/Flags.class */
class Flags {
    private Flags() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isEndOfStream(int flags) {
        return (flags & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAck(int flags) {
        return (flags & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isEndOfHeaders(int flags) {
        return (flags & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasPadding(int flags) {
        return (flags & 8) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasPriority(int flags) {
        return (flags & 32) != 0;
    }
}
