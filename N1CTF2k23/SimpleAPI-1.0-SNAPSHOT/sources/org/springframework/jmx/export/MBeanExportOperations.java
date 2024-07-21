package org.springframework.jmx.export;

import javax.management.ObjectName;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/MBeanExportOperations.class */
public interface MBeanExportOperations {
    ObjectName registerManagedResource(Object obj) throws MBeanExportException;

    void registerManagedResource(Object obj, ObjectName objectName) throws MBeanExportException;

    void unregisterManagedResource(ObjectName objectName);
}
