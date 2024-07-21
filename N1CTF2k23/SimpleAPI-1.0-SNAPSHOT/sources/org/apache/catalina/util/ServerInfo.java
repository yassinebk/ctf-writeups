package org.apache.catalina.util;

import java.io.InputStream;
import java.util.Properties;
import org.apache.tomcat.util.ExceptionUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/util/ServerInfo.class */
public class ServerInfo {
    private static final String serverInfo;
    private static final String serverBuilt;
    private static final String serverNumber;

    static {
        String info = null;
        String built = null;
        String number = null;
        Properties props = new Properties();
        try {
            InputStream is = ServerInfo.class.getResourceAsStream("/org/apache/catalina/util/ServerInfo.properties");
            props.load(is);
            info = props.getProperty("server.info");
            built = props.getProperty("server.built");
            number = props.getProperty("server.number");
            if (is != null) {
                if (0 != 0) {
                    is.close();
                } else {
                    is.close();
                }
            }
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
        }
        info = (info == null || info.equals("Apache Tomcat/@VERSION@")) ? "Apache Tomcat/9.0.x-dev" : "Apache Tomcat/9.0.x-dev";
        built = (built == null || built.equals("@VERSION_BUILT@")) ? "unknown" : "unknown";
        number = (number == null || number.equals("@VERSION_NUMBER@")) ? "9.0.x" : "9.0.x";
        serverInfo = info;
        serverBuilt = built;
        serverNumber = number;
    }

    public static String getServerInfo() {
        return serverInfo;
    }

    public static String getServerBuilt() {
        return serverBuilt;
    }

    public static String getServerNumber() {
        return serverNumber;
    }

    public static void main(String[] args) {
        System.out.println("Server version: " + getServerInfo());
        System.out.println("Server built:   " + getServerBuilt());
        System.out.println("Server number:  " + getServerNumber());
        System.out.println("OS Name:        " + System.getProperty("os.name"));
        System.out.println("OS Version:     " + System.getProperty("os.version"));
        System.out.println("Architecture:   " + System.getProperty("os.arch"));
        System.out.println("JVM Version:    " + System.getProperty("java.runtime.version"));
        System.out.println("JVM Vendor:     " + System.getProperty("java.vm.vendor"));
    }
}
