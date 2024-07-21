package org.apache.tomcat.util.descriptor.web;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/web/XmlEncodingBase.class */
public abstract class XmlEncodingBase {
    private Charset charset = StandardCharsets.UTF_8;

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return this.charset;
    }
}
