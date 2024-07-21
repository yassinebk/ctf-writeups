package org.apache.tomcat.util.compat;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/compat/JrePlatform.class */
public class JrePlatform {
    private static final String OS_NAME_PROPERTY = "os.name";
    public static final boolean IS_MAC_OS;
    public static final boolean IS_WINDOWS;

    static {
        String osName;
        if (System.getSecurityManager() == null) {
            osName = System.getProperty(OS_NAME_PROPERTY);
        } else {
            osName = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: org.apache.tomcat.util.compat.JrePlatform.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public String run() {
                    return System.getProperty(JrePlatform.OS_NAME_PROPERTY);
                }
            });
        }
        IS_MAC_OS = osName.toLowerCase(Locale.ENGLISH).startsWith("mac os x");
        IS_WINDOWS = osName.startsWith("Windows");
    }
}
