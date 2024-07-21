package org.springframework.core.env;

import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/env/EnumerablePropertySource.class */
public abstract class EnumerablePropertySource<T> extends PropertySource<T> {
    public abstract String[] getPropertyNames();

    public EnumerablePropertySource(String name, T source) {
        super(name, source);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EnumerablePropertySource(String name) {
        super(name);
    }

    @Override // org.springframework.core.env.PropertySource
    public boolean containsProperty(String name) {
        return ObjectUtils.containsElement(getPropertyNames(), name);
    }
}
