package org.apache.tomcat.util.descriptor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/Constants.class */
public class Constants {
    public static final String PACKAGE_NAME = Constants.class.getPackage().getName();
    public static final boolean IS_SECURITY_ENABLED;

    static {
        IS_SECURITY_ENABLED = System.getSecurityManager() != null;
    }
}
