package org.springframework.boot;

import java.io.PrintStream;
import org.springframework.core.env.Environment;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/Banner.class */
public interface Banner {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/Banner$Mode.class */
    public enum Mode {
        OFF,
        CONSOLE,
        LOG
    }

    void printBanner(Environment environment, Class<?> sourceClass, PrintStream out);
}
