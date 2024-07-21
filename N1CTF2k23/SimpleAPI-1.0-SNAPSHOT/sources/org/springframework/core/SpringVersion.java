package org.springframework.core;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/SpringVersion.class */
public final class SpringVersion {
    private SpringVersion() {
    }

    @Nullable
    public static String getVersion() {
        Package pkg = SpringVersion.class.getPackage();
        if (pkg != null) {
            return pkg.getImplementationVersion();
        }
        return null;
    }
}
