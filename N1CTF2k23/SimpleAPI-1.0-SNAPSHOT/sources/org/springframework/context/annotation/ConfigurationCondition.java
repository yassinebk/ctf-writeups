package org.springframework.context.annotation;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ConfigurationCondition.class */
public interface ConfigurationCondition extends Condition {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ConfigurationCondition$ConfigurationPhase.class */
    public enum ConfigurationPhase {
        PARSE_CONFIGURATION,
        REGISTER_BEAN
    }

    ConfigurationPhase getConfigurationPhase();
}
