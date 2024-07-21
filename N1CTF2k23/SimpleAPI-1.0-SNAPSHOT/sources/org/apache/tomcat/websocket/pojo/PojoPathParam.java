package org.apache.tomcat.websocket.pojo;

import javax.websocket.DeploymentException;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.websocket.Util;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/pojo/PojoPathParam.class */
public class PojoPathParam {
    private static final StringManager sm = StringManager.getManager(PojoPathParam.class);
    private final Class<?> type;
    private final String name;

    public PojoPathParam(Class<?> type, String name) throws DeploymentException {
        if (name != null) {
            validateType(type);
        }
        this.type = type;
        this.name = name;
    }

    public Class<?> getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    private static void validateType(Class<?> type) throws DeploymentException {
        if (String.class == type || Util.isPrimitive(type)) {
            return;
        }
        throw new DeploymentException(sm.getString("pojoPathParam.wrongType", type.getName()));
    }
}
