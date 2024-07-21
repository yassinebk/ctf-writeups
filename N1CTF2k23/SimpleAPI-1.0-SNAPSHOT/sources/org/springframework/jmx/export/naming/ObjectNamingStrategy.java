package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/naming/ObjectNamingStrategy.class */
public interface ObjectNamingStrategy {
    ObjectName getObjectName(Object obj, @Nullable String str) throws MalformedObjectNameException;
}
