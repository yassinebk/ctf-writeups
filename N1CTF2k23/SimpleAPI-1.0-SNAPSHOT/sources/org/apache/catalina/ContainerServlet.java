package org.apache.catalina;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/ContainerServlet.class */
public interface ContainerServlet {
    Wrapper getWrapper();

    void setWrapper(Wrapper wrapper);
}
