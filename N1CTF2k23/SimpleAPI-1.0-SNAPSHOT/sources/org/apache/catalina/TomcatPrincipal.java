package org.apache.catalina;

import java.security.Principal;
import org.ietf.jgss.GSSCredential;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/TomcatPrincipal.class */
public interface TomcatPrincipal extends Principal {
    Principal getUserPrincipal();

    GSSCredential getGssCredential();

    void logout() throws Exception;
}
