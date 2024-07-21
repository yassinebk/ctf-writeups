package org.apache.tomcat;

import java.io.File;
import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/JarScannerCallback.class */
public interface JarScannerCallback {
    void scan(Jar jar, String str, boolean z) throws IOException;

    void scan(File file, String str, boolean z) throws IOException;

    void scanWebInfClasses() throws IOException;
}
