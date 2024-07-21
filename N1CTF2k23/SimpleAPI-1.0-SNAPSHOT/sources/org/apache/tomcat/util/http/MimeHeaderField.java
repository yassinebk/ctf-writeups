package org.apache.tomcat.util.http;

import org.apache.tomcat.util.buf.MessageBytes;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MimeHeaders.java */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/MimeHeaderField.class */
public class MimeHeaderField {
    private final MessageBytes nameB = MessageBytes.newInstance();
    private final MessageBytes valueB = MessageBytes.newInstance();

    public void recycle() {
        this.nameB.recycle();
        this.valueB.recycle();
    }

    public MessageBytes getName() {
        return this.nameB;
    }

    public MessageBytes getValue() {
        return this.valueB;
    }
}
