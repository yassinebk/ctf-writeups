package org.springframework.boot;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/ExitCodeExceptionMapper.class */
public interface ExitCodeExceptionMapper {
    int getExitCode(Throwable exception);
}
