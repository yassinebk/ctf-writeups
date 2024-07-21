package org.apache.tomcat.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/file/ConfigurationSource.class */
public interface ConfigurationSource {
    public static final ConfigurationSource DEFAULT = new ConfigurationSource() { // from class: org.apache.tomcat.util.file.ConfigurationSource.1
        protected final File userDir = new File(System.getProperty("user.dir"));
        protected final URI userDirUri = this.userDir.toURI();

        @Override // org.apache.tomcat.util.file.ConfigurationSource
        public Resource getResource(String name) throws IOException {
            File f = new File(name);
            if (!f.isAbsolute()) {
                f = new File(this.userDir, name);
            }
            if (f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                return new Resource(fis, f.toURI());
            }
            try {
                URI uri = getURI(name);
                try {
                    URL url = uri.toURL();
                    return new Resource(url.openConnection().getInputStream(), uri);
                } catch (MalformedURLException e) {
                    throw new FileNotFoundException(name);
                }
            } catch (IllegalArgumentException e2) {
                throw new FileNotFoundException(name);
            }
        }

        @Override // org.apache.tomcat.util.file.ConfigurationSource
        public URI getURI(String name) {
            File f = new File(name);
            if (!f.isAbsolute()) {
                f = new File(this.userDir, name);
            }
            if (f.isFile()) {
                return f.toURI();
            }
            return this.userDirUri.resolve(name);
        }
    };

    Resource getResource(String str) throws IOException;

    URI getURI(String str);

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/file/ConfigurationSource$Resource.class */
    public static class Resource implements AutoCloseable {
        private final InputStream inputStream;
        private final URI uri;

        public Resource(InputStream inputStream, URI uri) {
            this.inputStream = inputStream;
            this.uri = uri;
        }

        public InputStream getInputStream() {
            return this.inputStream;
        }

        public URI getURI() {
            return this.uri;
        }

        public long getLastModified() throws MalformedURLException, IOException {
            return this.uri.toURL().openConnection().getLastModified();
        }

        @Override // java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
        }
    }

    default Resource getServerXml() throws IOException {
        return getConfResource("server.xml");
    }

    default Resource getSharedWebXml() throws IOException {
        return getConfResource("web.xml");
    }

    default Resource getConfResource(String name) throws IOException {
        String fullName = "conf/" + name;
        return getResource(fullName);
    }
}
