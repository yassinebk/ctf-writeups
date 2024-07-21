package org.apache.catalina.realm;

import java.security.Principal;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/realm/NullRealm.class */
public class NullRealm extends RealmBase {
    @Override // org.apache.catalina.realm.RealmBase
    protected String getPassword(String username) {
        return null;
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected Principal getPrincipal(String username) {
        return null;
    }
}
