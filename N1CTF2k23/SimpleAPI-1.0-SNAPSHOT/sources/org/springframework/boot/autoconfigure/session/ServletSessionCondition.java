package org.springframework.boot.autoconfigure.session;

import org.springframework.boot.WebApplicationType;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/session/ServletSessionCondition.class */
class ServletSessionCondition extends AbstractSessionCondition {
    ServletSessionCondition() {
        super(WebApplicationType.SERVLET);
    }
}
