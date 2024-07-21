package org.apache.tomcat;

import javax.servlet.ServletContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/JarScanner.class */
public interface JarScanner {
    void scan(JarScanType jarScanType, ServletContext servletContext, JarScannerCallback jarScannerCallback);

    JarScanFilter getJarScanFilter();

    void setJarScanFilter(JarScanFilter jarScanFilter);
}
