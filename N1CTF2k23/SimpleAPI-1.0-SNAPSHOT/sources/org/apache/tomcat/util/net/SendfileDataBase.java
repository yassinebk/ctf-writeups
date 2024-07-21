package org.apache.tomcat.util.net;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/SendfileDataBase.class */
public abstract class SendfileDataBase {
    public SendfileKeepAliveState keepAliveState = SendfileKeepAliveState.NONE;
    public final String fileName;
    public long pos;
    public long length;

    public SendfileDataBase(String filename, long pos, long length) {
        this.fileName = filename;
        this.pos = pos;
        this.length = length;
    }
}
