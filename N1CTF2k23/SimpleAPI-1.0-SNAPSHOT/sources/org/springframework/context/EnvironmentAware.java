package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.env.Environment;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/EnvironmentAware.class */
public interface EnvironmentAware extends Aware {
    void setEnvironment(Environment environment);
}
