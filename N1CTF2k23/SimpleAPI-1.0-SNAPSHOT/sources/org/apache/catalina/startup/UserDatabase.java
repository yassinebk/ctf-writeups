package org.apache.catalina.startup;

import java.util.Enumeration;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/UserDatabase.class */
public interface UserDatabase {
    UserConfig getUserConfig();

    void setUserConfig(UserConfig userConfig);

    String getHome(String str);

    Enumeration<String> getUsers();
}
