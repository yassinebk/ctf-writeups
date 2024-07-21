package org.springframework.boot.admin;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/admin/SpringApplicationAdminMXBean.class */
public interface SpringApplicationAdminMXBean {
    boolean isReady();

    boolean isEmbeddedWebApplication();

    String getProperty(String key);

    void shutdown();
}
