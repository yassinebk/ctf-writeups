package org.apache.catalina;

import javax.management.MBeanRegistration;
import javax.management.ObjectName;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/JmxEnabled.class */
public interface JmxEnabled extends MBeanRegistration {
    String getDomain();

    void setDomain(String str);

    ObjectName getObjectName();
}
