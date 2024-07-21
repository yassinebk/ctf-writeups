package org.springframework.core;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/AliasRegistry.class */
public interface AliasRegistry {
    void registerAlias(String str, String str2);

    void removeAlias(String str);

    boolean isAlias(String str);

    String[] getAliases(String str);
}
