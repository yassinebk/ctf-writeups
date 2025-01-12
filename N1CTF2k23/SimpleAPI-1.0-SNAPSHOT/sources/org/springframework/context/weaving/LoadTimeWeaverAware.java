package org.springframework.context.weaving;

import org.springframework.beans.factory.Aware;
import org.springframework.instrument.classloading.LoadTimeWeaver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/weaving/LoadTimeWeaverAware.class */
public interface LoadTimeWeaverAware extends Aware {
    void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver);
}
