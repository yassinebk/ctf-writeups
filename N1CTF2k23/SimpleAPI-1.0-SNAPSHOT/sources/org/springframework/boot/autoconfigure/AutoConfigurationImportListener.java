package org.springframework.boot.autoconfigure;

import java.util.EventListener;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/AutoConfigurationImportListener.class */
public interface AutoConfigurationImportListener extends EventListener {
    void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event);
}
