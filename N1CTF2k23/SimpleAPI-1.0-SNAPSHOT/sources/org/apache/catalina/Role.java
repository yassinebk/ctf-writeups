package org.apache.catalina;

import java.security.Principal;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/Role.class */
public interface Role extends Principal {
    String getDescription();

    void setDescription(String str);

    String getRolename();

    void setRolename(String str);

    UserDatabase getUserDatabase();
}
