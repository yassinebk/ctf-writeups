package org.springframework.core;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/DefaultParameterNameDiscoverer.class */
public class DefaultParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer {
    public DefaultParameterNameDiscoverer() {
        if (KotlinDetector.isKotlinReflectPresent() && !GraalDetector.inImageCode()) {
            addDiscoverer(new KotlinReflectionParameterNameDiscoverer());
        }
        addDiscoverer(new StandardReflectionParameterNameDiscoverer());
        addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
    }
}
