package org.apache.tomcat.util.modeler;

import java.util.List;
import javax.management.ObjectName;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/modeler/RegistryMBean.class */
public interface RegistryMBean {
    void invoke(List<ObjectName> list, String str, boolean z) throws Exception;

    void registerComponent(Object obj, String str, String str2) throws Exception;

    void unregisterComponent(String str);

    int getId(String str, String str2);

    void stop();
}
