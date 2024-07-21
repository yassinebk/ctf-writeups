package org.apache.catalina;

import java.security.Principal;
import java.util.Iterator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/User.class */
public interface User extends Principal {
    String getFullName();

    void setFullName(String str);

    Iterator<Group> getGroups();

    String getPassword();

    void setPassword(String str);

    Iterator<Role> getRoles();

    UserDatabase getUserDatabase();

    String getUsername();

    void setUsername(String str);

    void addGroup(Group group);

    void addRole(Role role);

    boolean isInGroup(Group group);

    boolean isInRole(Role role);

    void removeGroup(Group group);

    void removeGroups();

    void removeRole(Role role);

    void removeRoles();
}
