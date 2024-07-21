package javax.websocket;

import java.util.List;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/Extension.class */
public interface Extension {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/Extension$Parameter.class */
    public interface Parameter {
        String getName();

        String getValue();
    }

    String getName();

    List<Parameter> getParameters();
}
