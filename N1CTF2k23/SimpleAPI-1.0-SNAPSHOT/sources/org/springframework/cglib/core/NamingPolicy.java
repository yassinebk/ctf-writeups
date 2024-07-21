package org.springframework.cglib.core;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/NamingPolicy.class */
public interface NamingPolicy {
    String getClassName(String str, String str2, Object obj, Predicate predicate);

    boolean equals(Object obj);
}
