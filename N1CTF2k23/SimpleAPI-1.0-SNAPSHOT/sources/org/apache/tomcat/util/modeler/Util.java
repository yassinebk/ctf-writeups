package org.apache.tomcat.util.modeler;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/modeler/Util.class */
public class Util {
    private Util() {
    }

    public static boolean objectNameValueNeedsQuote(String input) {
        for (int i = 0; i < input.length(); i++) {
            char ch2 = input.charAt(i);
            if (ch2 == ',' || ch2 == '=' || ch2 == ':' || ch2 == '*' || ch2 == '?') {
                return true;
            }
        }
        return false;
    }
}
