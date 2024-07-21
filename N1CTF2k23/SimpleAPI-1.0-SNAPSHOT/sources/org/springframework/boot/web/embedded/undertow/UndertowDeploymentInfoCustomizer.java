package org.springframework.boot.web.embedded.undertow;

import io.undertow.servlet.api.DeploymentInfo;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowDeploymentInfoCustomizer.class */
public interface UndertowDeploymentInfoCustomizer {
    void customize(DeploymentInfo deploymentInfo);
}
