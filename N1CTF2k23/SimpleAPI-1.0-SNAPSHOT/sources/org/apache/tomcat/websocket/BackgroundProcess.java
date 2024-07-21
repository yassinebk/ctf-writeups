package org.apache.tomcat.websocket;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/BackgroundProcess.class */
public interface BackgroundProcess {
    void backgroundProcess();

    void setProcessPeriod(int i);

    int getProcessPeriod();
}
