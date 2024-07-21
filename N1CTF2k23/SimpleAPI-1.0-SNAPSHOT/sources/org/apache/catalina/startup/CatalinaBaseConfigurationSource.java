package org.apache.catalina.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/CatalinaBaseConfigurationSource.class */
public class CatalinaBaseConfigurationSource implements ConfigurationSource {
    protected static final StringManager sm = StringManager.getManager(Constants.Package);
    private final String serverXmlPath;
    private final File catalinaBaseFile;
    private final URI catalinaBaseUri;

    public CatalinaBaseConfigurationSource(File catalinaBaseFile, String serverXmlPath) {
        this.catalinaBaseFile = catalinaBaseFile;
        this.catalinaBaseUri = catalinaBaseFile.toURI();
        this.serverXmlPath = serverXmlPath;
    }

    @Override // org.apache.tomcat.util.file.ConfigurationSource
    public ConfigurationSource.Resource getServerXml() throws IOException {
        InputStream stream;
        IOException ioe = null;
        ConfigurationSource.Resource result = null;
        try {
            if (this.serverXmlPath == null || this.serverXmlPath.equals(Catalina.SERVER_XML)) {
                result = super.getServerXml();
            } else {
                result = getResource(this.serverXmlPath);
            }
        } catch (IOException e) {
            ioe = e;
        }
        if (result == null && (stream = getClass().getClassLoader().getResourceAsStream("server-embed.xml")) != null) {
            try {
                result = new ConfigurationSource.Resource(stream, getClass().getClassLoader().getResource("server-embed.xml").toURI());
            } catch (URISyntaxException e2) {
                stream.close();
            }
        }
        if (result == null && ioe != null) {
            throw ioe;
        }
        return result;
    }

    @Override // org.apache.tomcat.util.file.ConfigurationSource
    public ConfigurationSource.Resource getResource(String name) throws IOException {
        File f = new File(name);
        if (!f.isAbsolute()) {
            f = new File(this.catalinaBaseFile, name);
        }
        if (f.isFile()) {
            FileInputStream fis = new FileInputStream(f);
            return new ConfigurationSource.Resource(fis, f.toURI());
        }
        InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
        if (stream != null) {
            try {
                return new ConfigurationSource.Resource(stream, getClass().getClassLoader().getResource(name).toURI());
            } catch (URISyntaxException e) {
                stream.close();
                throw new IOException(sm.getString("catalinaConfigurationSource.cannotObtainURL", name), e);
            } catch (InvalidPathException e2) {
                stream.close();
            }
        }
        try {
            URI uri = getURI(name);
            try {
                URL url = uri.toURL();
                return new ConfigurationSource.Resource(url.openConnection().getInputStream(), uri);
            } catch (MalformedURLException e3) {
                throw new IOException(sm.getString("catalinaConfigurationSource.cannotObtainURL", name), e3);
            }
        } catch (IllegalArgumentException e4) {
            throw new IOException(sm.getString("catalinaConfigurationSource.cannotObtainURL", name), e4);
        }
    }

    @Override // org.apache.tomcat.util.file.ConfigurationSource
    public URI getURI(String name) {
        URI uri;
        File f = new File(name);
        if (!f.isAbsolute()) {
            f = new File(this.catalinaBaseFile, name);
        }
        if (f.isFile()) {
            return f.toURI();
        }
        try {
            URL resource = getClass().getClassLoader().getResource(name);
            if (resource != null) {
                return resource.toURI();
            }
        } catch (Exception e) {
        }
        if (this.catalinaBaseUri != null) {
            uri = this.catalinaBaseUri.resolve(name);
        } else {
            uri = URI.create(name);
        }
        return uri;
    }
}
