package org.springframework.boot.loader.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/AbstractJarFile.class */
abstract class AbstractJarFile extends java.util.jar.JarFile {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/AbstractJarFile$JarFileType.class */
    enum JarFileType {
        DIRECT,
        NESTED_DIRECTORY,
        NESTED_JAR
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract URL getUrl() throws MalformedURLException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract JarFileType getType();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Permission getPermission();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract InputStream getInputStream() throws IOException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractJarFile(File file) throws IOException {
        super(file);
    }
}
