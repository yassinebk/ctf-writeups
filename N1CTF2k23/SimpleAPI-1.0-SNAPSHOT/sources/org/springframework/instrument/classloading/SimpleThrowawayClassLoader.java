package org.springframework.instrument.classloading;

import org.springframework.core.OverridingClassLoader;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/instrument/classloading/SimpleThrowawayClassLoader.class */
public class SimpleThrowawayClassLoader extends OverridingClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public SimpleThrowawayClassLoader(@Nullable ClassLoader parent) {
        super(parent);
    }
}
