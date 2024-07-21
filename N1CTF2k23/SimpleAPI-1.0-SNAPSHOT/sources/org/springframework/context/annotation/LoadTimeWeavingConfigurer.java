package org.springframework.context.annotation;

import org.springframework.instrument.classloading.LoadTimeWeaver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/LoadTimeWeavingConfigurer.class */
public interface LoadTimeWeavingConfigurer {
    LoadTimeWeaver getLoadTimeWeaver();
}
