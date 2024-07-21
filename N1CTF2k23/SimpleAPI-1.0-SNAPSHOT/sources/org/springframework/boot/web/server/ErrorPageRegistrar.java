package org.springframework.boot.web.server;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/server/ErrorPageRegistrar.class */
public interface ErrorPageRegistrar {
    void registerErrorPages(ErrorPageRegistry registry);
}