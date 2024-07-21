package org.springframework.boot.autoconfigure.jms.artemis;

import org.apache.activemq.artemis.spi.core.naming.BindingRegistry;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jms/artemis/ArtemisNoOpBindingRegistry.class */
public class ArtemisNoOpBindingRegistry implements BindingRegistry {
    public Object lookup(String s) {
        return null;
    }

    public boolean bind(String s, Object o) {
        return false;
    }

    public void unbind(String s) {
    }

    public void close() {
    }
}
