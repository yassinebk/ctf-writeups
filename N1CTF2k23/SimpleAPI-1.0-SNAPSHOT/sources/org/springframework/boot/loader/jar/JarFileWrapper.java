package org.springframework.boot.loader.jar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.Enumeration;
import java.util.jar.Manifest;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import org.springframework.boot.loader.jar.AbstractJarFile;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/JarFileWrapper.class */
public class JarFileWrapper extends AbstractJarFile {
    private final JarFile parent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public JarFileWrapper(JarFile parent) throws IOException {
        super(parent.getRootJarFile().getFile());
        this.parent = parent;
        if (System.getSecurityManager() == null) {
            super.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.springframework.boot.loader.jar.AbstractJarFile
    public URL getUrl() throws MalformedURLException {
        return this.parent.getUrl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.springframework.boot.loader.jar.AbstractJarFile
    public AbstractJarFile.JarFileType getType() {
        return this.parent.getType();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.springframework.boot.loader.jar.AbstractJarFile
    public Permission getPermission() {
        return this.parent.getPermission();
    }

    @Override // java.util.jar.JarFile
    public Manifest getManifest() throws IOException {
        return this.parent.getManifest();
    }

    @Override // java.util.jar.JarFile, java.util.zip.ZipFile
    public Enumeration<java.util.jar.JarEntry> entries() {
        return this.parent.entries();
    }

    @Override // java.util.jar.JarFile, java.util.zip.ZipFile
    public Stream<java.util.jar.JarEntry> stream() {
        return this.parent.stream();
    }

    @Override // java.util.jar.JarFile
    public java.util.jar.JarEntry getJarEntry(String name) {
        return this.parent.getJarEntry(name);
    }

    @Override // java.util.jar.JarFile, java.util.zip.ZipFile
    public ZipEntry getEntry(String name) {
        return this.parent.getEntry(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.springframework.boot.loader.jar.AbstractJarFile
    public InputStream getInputStream() throws IOException {
        return this.parent.getInputStream();
    }

    @Override // java.util.jar.JarFile, java.util.zip.ZipFile
    public synchronized InputStream getInputStream(ZipEntry ze) throws IOException {
        return this.parent.getInputStream(ze);
    }

    @Override // java.util.zip.ZipFile
    public String getComment() {
        return this.parent.getComment();
    }

    @Override // java.util.zip.ZipFile
    public int size() {
        return this.parent.size();
    }

    public String toString() {
        return this.parent.toString();
    }

    @Override // java.util.zip.ZipFile
    public String getName() {
        return this.parent.getName();
    }

    static JarFile unwrap(java.util.jar.JarFile jarFile) {
        if (jarFile instanceof JarFile) {
            return (JarFile) jarFile;
        }
        if (jarFile instanceof JarFileWrapper) {
            return unwrap(((JarFileWrapper) jarFile).parent);
        }
        throw new IllegalStateException("Not a JarFile or Wrapper");
    }
}
