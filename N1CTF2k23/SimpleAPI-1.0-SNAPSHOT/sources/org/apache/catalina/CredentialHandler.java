package org.apache.catalina;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/CredentialHandler.class */
public interface CredentialHandler {
    boolean matches(String str, String str2);

    String mutate(String str);
}
