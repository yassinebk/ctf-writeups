package org.springframework.jndi;

import javax.naming.NamingException;
import org.springframework.core.NestedRuntimeException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jndi/JndiLookupFailureException.class */
public class JndiLookupFailureException extends NestedRuntimeException {
    public JndiLookupFailureException(String msg, NamingException cause) {
        super(msg, cause);
    }
}
