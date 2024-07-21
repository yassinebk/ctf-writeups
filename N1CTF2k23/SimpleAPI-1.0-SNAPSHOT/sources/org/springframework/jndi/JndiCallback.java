package org.springframework.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jndi/JndiCallback.class */
public interface JndiCallback<T> {
    @Nullable
    T doInContext(Context context) throws NamingException;
}
