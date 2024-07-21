package org.springframework.core;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/GraalDetector.class */
abstract class GraalDetector {
    private static final boolean imageCode;

    GraalDetector() {
    }

    static {
        imageCode = System.getProperty("org.graalvm.nativeimage.imagecode") != null;
    }

    public static boolean inImageCode() {
        return imageCode;
    }
}
