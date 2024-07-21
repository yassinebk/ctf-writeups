package org.apache.catalina;

import java.io.InputStream;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Manifest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/WebResource.class */
public interface WebResource {
    long getLastModified();

    String getLastModifiedHttp();

    boolean exists();

    boolean isVirtual();

    boolean isDirectory();

    boolean isFile();

    boolean delete();

    String getName();

    long getContentLength();

    String getCanonicalPath();

    boolean canRead();

    String getWebappPath();

    String getETag();

    void setMimeType(String str);

    String getMimeType();

    InputStream getInputStream();

    byte[] getContent();

    long getCreation();

    URL getURL();

    URL getCodeBase();

    WebResourceRoot getWebResourceRoot();

    Certificate[] getCertificates();

    Manifest getManifest();
}
