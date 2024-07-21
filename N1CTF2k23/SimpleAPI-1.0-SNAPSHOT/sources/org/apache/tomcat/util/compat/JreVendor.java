package org.apache.tomcat.util.compat;

import java.util.Locale;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/compat/JreVendor.class */
public class JreVendor {
    public static final boolean IS_ORACLE_JVM;
    public static final boolean IS_IBM_JVM;

    static {
        String vendor = System.getProperty("java.vendor", "").toLowerCase(Locale.ENGLISH);
        if (vendor.startsWith("oracle") || vendor.startsWith("sun")) {
            IS_ORACLE_JVM = true;
            IS_IBM_JVM = false;
        } else if (vendor.contains("ibm")) {
            IS_ORACLE_JVM = false;
            IS_IBM_JVM = true;
        } else {
            IS_ORACLE_JVM = false;
            IS_IBM_JVM = false;
        }
    }
}
