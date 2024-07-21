package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/naming/SelfNaming.class */
public interface SelfNaming {
    ObjectName getObjectName() throws MalformedObjectNameException;
}
