package org.springframework.core;

import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/NamedInheritableThreadLocal.class */
public class NamedInheritableThreadLocal<T> extends InheritableThreadLocal<T> {
    private final String name;

    public NamedInheritableThreadLocal(String name) {
        Assert.hasText(name, "Name must not be empty");
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
