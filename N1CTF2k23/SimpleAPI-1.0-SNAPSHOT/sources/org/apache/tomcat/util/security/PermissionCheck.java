package org.apache.tomcat.util.security;

import java.security.Permission;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/security/PermissionCheck.class */
public interface PermissionCheck {
    boolean check(Permission permission);
}
