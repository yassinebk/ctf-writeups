package javax.servlet;

import java.util.Map;
import java.util.Set;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/Registration.class */
public interface Registration {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/Registration$Dynamic.class */
    public interface Dynamic extends Registration {
        void setAsyncSupported(boolean z);
    }

    String getName();

    String getClassName();

    boolean setInitParameter(String str, String str2);

    String getInitParameter(String str);

    Set<String> setInitParameters(Map<String, String> map);

    Map<String, String> getInitParameters();
}
