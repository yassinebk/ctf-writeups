package org.springframework.core.env;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/env/Environment.class */
public interface Environment extends PropertyResolver {
    String[] getActiveProfiles();

    String[] getDefaultProfiles();

    @Deprecated
    boolean acceptsProfiles(String... strArr);

    boolean acceptsProfiles(Profiles profiles);
}
