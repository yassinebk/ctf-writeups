package org.apache.catalina.valves.rewrite;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/valves/rewrite/RewriteMap.class */
public interface RewriteMap {
    String setParameters(String str);

    String lookup(String str);

    default void setParameters(String... params) {
        if (params == null) {
            return;
        }
        if (params.length > 1) {
            throw new IllegalArgumentException("Too many parameters for this map");
        }
        setParameters(params[0]);
    }
}
