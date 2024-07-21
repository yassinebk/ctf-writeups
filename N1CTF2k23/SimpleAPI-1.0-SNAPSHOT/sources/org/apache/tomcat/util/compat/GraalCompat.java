package org.apache.tomcat.util.compat;

import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/compat/GraalCompat.class */
class GraalCompat extends Jre9Compat {
    private static final boolean GRAAL;

    static {
        boolean result = false;
        try {
            Class<?> nativeImageClazz = Class.forName("org.graalvm.nativeimage.ImageInfo");
            result = Boolean.TRUE.equals(nativeImageClazz.getMethod("inImageCode", new Class[0]).invoke(null, new Object[0]));
        } catch (ClassNotFoundException e) {
        } catch (IllegalArgumentException | ReflectiveOperationException e2) {
        }
        GRAAL = result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSupported() {
        return GRAAL;
    }

    @Override // org.apache.tomcat.util.compat.Jre9Compat, org.apache.tomcat.util.compat.JreCompat
    public void disableCachingForJarUrlConnections() throws IOException {
    }
}
