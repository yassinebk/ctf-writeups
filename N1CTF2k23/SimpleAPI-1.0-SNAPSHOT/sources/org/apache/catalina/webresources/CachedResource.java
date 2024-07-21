package org.apache.catalina.webresources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.security.Permission;
import java.security.cert.Certificate;
import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;
import org.springframework.util.ResourceUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/webresources/CachedResource.class */
public class CachedResource implements WebResource {
    private static final Log log = LogFactory.getLog(CachedResource.class);
    private static final StringManager sm = StringManager.getManager(CachedResource.class);
    private static final long CACHE_ENTRY_SIZE = 500;
    private final Cache cache;
    private final StandardRoot root;
    private final String webAppPath;
    private final long ttl;
    private final int objectMaxSizeBytes;
    private final boolean usesClassLoaderResources;
    private volatile WebResource webResource;
    private volatile WebResource[] webResources;
    private volatile long nextCheck;
    private volatile Long cachedLastModified = null;
    private volatile String cachedLastModifiedHttp = null;
    private volatile byte[] cachedContent = null;
    private volatile Boolean cachedIsFile = null;
    private volatile Boolean cachedIsDirectory = null;
    private volatile Boolean cachedExists = null;
    private volatile Boolean cachedIsVirtual = null;
    private volatile Long cachedContentLength = null;

    public CachedResource(Cache cache, StandardRoot root, String path, long ttl, int objectMaxSizeBytes, boolean usesClassLoaderResources) {
        this.cache = cache;
        this.root = root;
        this.webAppPath = path;
        this.ttl = ttl;
        this.objectMaxSizeBytes = objectMaxSizeBytes;
        this.usesClassLoaderResources = usesClassLoaderResources;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean validateResource(boolean useClassLoaderResources) {
        if (this.usesClassLoaderResources != useClassLoaderResources) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (this.webResource == null) {
            synchronized (this) {
                if (this.webResource == null) {
                    this.webResource = this.root.getResourceInternal(this.webAppPath, useClassLoaderResources);
                    getLastModified();
                    getContentLength();
                    this.nextCheck = this.ttl + now;
                    if (this.webResource instanceof EmptyResource) {
                        this.cachedExists = Boolean.FALSE;
                    } else {
                        this.cachedExists = Boolean.TRUE;
                    }
                    return true;
                }
            }
        }
        if (now < this.nextCheck) {
            return true;
        }
        if (!this.root.isPackedWarFile()) {
            WebResource webResourceInternal = this.root.getResourceInternal(this.webAppPath, useClassLoaderResources);
            if ((!this.webResource.exists() && webResourceInternal.exists()) || this.webResource.getLastModified() != getLastModified() || this.webResource.getContentLength() != getContentLength() || this.webResource.getLastModified() != webResourceInternal.getLastModified() || this.webResource.getContentLength() != webResourceInternal.getContentLength()) {
                return false;
            }
        }
        this.nextCheck = this.ttl + now;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean validateResources(boolean useClassLoaderResources) {
        long now = System.currentTimeMillis();
        if (this.webResources == null) {
            synchronized (this) {
                if (this.webResources == null) {
                    this.webResources = this.root.getResourcesInternal(this.webAppPath, useClassLoaderResources);
                    this.nextCheck = this.ttl + now;
                    return true;
                }
            }
        }
        if (now < this.nextCheck) {
            return true;
        }
        if (this.root.isPackedWarFile()) {
            this.nextCheck = this.ttl + now;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getNextCheck() {
        return this.nextCheck;
    }

    @Override // org.apache.catalina.WebResource
    public long getLastModified() {
        Long cachedLastModified = this.cachedLastModified;
        if (cachedLastModified == null) {
            cachedLastModified = Long.valueOf(this.webResource.getLastModified());
            this.cachedLastModified = cachedLastModified;
        }
        return cachedLastModified.longValue();
    }

    @Override // org.apache.catalina.WebResource
    public String getLastModifiedHttp() {
        String cachedLastModifiedHttp = this.cachedLastModifiedHttp;
        if (cachedLastModifiedHttp == null) {
            cachedLastModifiedHttp = this.webResource.getLastModifiedHttp();
            this.cachedLastModifiedHttp = cachedLastModifiedHttp;
        }
        return cachedLastModifiedHttp;
    }

    @Override // org.apache.catalina.WebResource
    public boolean exists() {
        Boolean cachedExists = this.cachedExists;
        if (cachedExists == null) {
            cachedExists = Boolean.valueOf(this.webResource.exists());
            this.cachedExists = cachedExists;
        }
        return cachedExists.booleanValue();
    }

    @Override // org.apache.catalina.WebResource
    public boolean isVirtual() {
        Boolean cachedIsVirtual = this.cachedIsVirtual;
        if (cachedIsVirtual == null) {
            cachedIsVirtual = Boolean.valueOf(this.webResource.isVirtual());
            this.cachedIsVirtual = cachedIsVirtual;
        }
        return cachedIsVirtual.booleanValue();
    }

    @Override // org.apache.catalina.WebResource
    public boolean isDirectory() {
        Boolean cachedIsDirectory = this.cachedIsDirectory;
        if (cachedIsDirectory == null) {
            cachedIsDirectory = Boolean.valueOf(this.webResource.isDirectory());
            this.cachedIsDirectory = cachedIsDirectory;
        }
        return cachedIsDirectory.booleanValue();
    }

    @Override // org.apache.catalina.WebResource
    public boolean isFile() {
        Boolean cachedIsFile = this.cachedIsFile;
        if (cachedIsFile == null) {
            cachedIsFile = Boolean.valueOf(this.webResource.isFile());
            this.cachedIsFile = cachedIsFile;
        }
        return cachedIsFile.booleanValue();
    }

    @Override // org.apache.catalina.WebResource
    public boolean delete() {
        boolean deleteResult = this.webResource.delete();
        if (deleteResult) {
            this.cache.removeCacheEntry(this.webAppPath);
        }
        return deleteResult;
    }

    @Override // org.apache.catalina.WebResource
    public String getName() {
        return this.webResource.getName();
    }

    @Override // org.apache.catalina.WebResource
    public long getContentLength() {
        Long cachedContentLength = this.cachedContentLength;
        if (cachedContentLength == null) {
            long result = 0;
            if (this.webResource != null) {
                result = this.webResource.getContentLength();
                this.cachedContentLength = Long.valueOf(result);
            }
            return result;
        }
        return cachedContentLength.longValue();
    }

    @Override // org.apache.catalina.WebResource
    public String getCanonicalPath() {
        return this.webResource.getCanonicalPath();
    }

    @Override // org.apache.catalina.WebResource
    public boolean canRead() {
        return this.webResource.canRead();
    }

    @Override // org.apache.catalina.WebResource
    public String getWebappPath() {
        return this.webAppPath;
    }

    @Override // org.apache.catalina.WebResource
    public String getETag() {
        return this.webResource.getETag();
    }

    @Override // org.apache.catalina.WebResource
    public void setMimeType(String mimeType) {
        this.webResource.setMimeType(mimeType);
    }

    @Override // org.apache.catalina.WebResource
    public String getMimeType() {
        return this.webResource.getMimeType();
    }

    @Override // org.apache.catalina.WebResource
    public InputStream getInputStream() {
        byte[] content = getContent();
        if (content == null) {
            return this.webResource.getInputStream();
        }
        return new ByteArrayInputStream(content);
    }

    @Override // org.apache.catalina.WebResource
    public byte[] getContent() {
        byte[] cachedContent = this.cachedContent;
        if (cachedContent == null) {
            if (getContentLength() > this.objectMaxSizeBytes) {
                return null;
            }
            cachedContent = this.webResource.getContent();
            this.cachedContent = cachedContent;
        }
        return cachedContent;
    }

    @Override // org.apache.catalina.WebResource
    public long getCreation() {
        return this.webResource.getCreation();
    }

    @Override // org.apache.catalina.WebResource
    public URL getURL() {
        URL resourceURL = this.webResource.getURL();
        if (resourceURL == null) {
            return null;
        }
        try {
            CachedResourceURLStreamHandler handler = new CachedResourceURLStreamHandler(resourceURL, this.root, this.webAppPath, this.usesClassLoaderResources);
            URL result = new URL((URL) null, resourceURL.toExternalForm(), handler);
            handler.setAssociatedURL(result);
            return result;
        } catch (MalformedURLException e) {
            log.error(sm.getString("cachedResource.invalidURL", resourceURL.toExternalForm()), e);
            return null;
        }
    }

    @Override // org.apache.catalina.WebResource
    public URL getCodeBase() {
        return this.webResource.getCodeBase();
    }

    @Override // org.apache.catalina.WebResource
    public Certificate[] getCertificates() {
        return this.webResource.getCertificates();
    }

    @Override // org.apache.catalina.WebResource
    public Manifest getManifest() {
        return this.webResource.getManifest();
    }

    @Override // org.apache.catalina.WebResource
    public WebResourceRoot getWebResourceRoot() {
        return this.webResource.getWebResourceRoot();
    }

    WebResource getWebResource() {
        return this.webResource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebResource[] getWebResources() {
        return this.webResources;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getSize() {
        long result = 500;
        if (getContentLength() <= this.objectMaxSizeBytes) {
            result = CACHE_ENTRY_SIZE + getContentLength();
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static InputStream buildInputStream(String[] files) {
        Arrays.sort(files, Collator.getInstance(Locale.getDefault()));
        StringBuilder result = new StringBuilder();
        for (String file : files) {
            result.append(file);
            result.append('\n');
        }
        return new ByteArrayInputStream(result.toString().getBytes(Charset.defaultCharset()));
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/webresources/CachedResource$CachedResourceURLStreamHandler.class */
    private static class CachedResourceURLStreamHandler extends URLStreamHandler {
        private final URL resourceURL;
        private final StandardRoot root;
        private final String webAppPath;
        private final boolean usesClassLoaderResources;
        private URL associatedURL = null;

        public CachedResourceURLStreamHandler(URL resourceURL, StandardRoot root, String webAppPath, boolean usesClassLoaderResources) {
            this.resourceURL = resourceURL;
            this.root = root;
            this.webAppPath = webAppPath;
            this.usesClassLoaderResources = usesClassLoaderResources;
        }

        protected void setAssociatedURL(URL associatedURL) {
            this.associatedURL = associatedURL;
        }

        @Override // java.net.URLStreamHandler
        protected URLConnection openConnection(URL u) throws IOException {
            if (this.associatedURL != null && u == this.associatedURL) {
                if (ResourceUtils.URL_PROTOCOL_JAR.equals(this.associatedURL.getProtocol())) {
                    return new CachedResourceJarURLConnection(this.resourceURL, this.root, this.webAppPath, this.usesClassLoaderResources);
                }
                return new CachedResourceURLConnection(this.resourceURL, this.root, this.webAppPath, this.usesClassLoaderResources);
            }
            URL constructedURL = new URL(u.toExternalForm());
            return constructedURL.openConnection();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/webresources/CachedResource$CachedResourceURLConnection.class */
    private static class CachedResourceURLConnection extends URLConnection {
        private final StandardRoot root;
        private final String webAppPath;
        private final boolean usesClassLoaderResources;
        private final URL resourceURL;

        protected CachedResourceURLConnection(URL resourceURL, StandardRoot root, String webAppPath, boolean usesClassLoaderResources) {
            super(resourceURL);
            this.root = root;
            this.webAppPath = webAppPath;
            this.usesClassLoaderResources = usesClassLoaderResources;
            this.resourceURL = resourceURL;
        }

        @Override // java.net.URLConnection
        public void connect() throws IOException {
        }

        @Override // java.net.URLConnection
        public InputStream getInputStream() throws IOException {
            WebResource resource = getResource();
            if (resource.isDirectory()) {
                return CachedResource.buildInputStream(resource.getWebResourceRoot().list(this.webAppPath));
            }
            return getResource().getInputStream();
        }

        @Override // java.net.URLConnection
        public Permission getPermission() throws IOException {
            return this.resourceURL.openConnection().getPermission();
        }

        @Override // java.net.URLConnection
        public long getLastModified() {
            return getResource().getLastModified();
        }

        @Override // java.net.URLConnection
        public long getContentLengthLong() {
            return getResource().getContentLength();
        }

        private WebResource getResource() {
            return this.root.getResource(this.webAppPath, false, this.usesClassLoaderResources);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/webresources/CachedResource$CachedResourceJarURLConnection.class */
    private static class CachedResourceJarURLConnection extends JarURLConnection {
        private final StandardRoot root;
        private final String webAppPath;
        private final boolean usesClassLoaderResources;
        private final URL resourceURL;

        protected CachedResourceJarURLConnection(URL resourceURL, StandardRoot root, String webAppPath, boolean usesClassLoaderResources) throws IOException {
            super(resourceURL);
            this.root = root;
            this.webAppPath = webAppPath;
            this.usesClassLoaderResources = usesClassLoaderResources;
            this.resourceURL = resourceURL;
        }

        @Override // java.net.URLConnection
        public void connect() throws IOException {
        }

        @Override // java.net.URLConnection
        public InputStream getInputStream() throws IOException {
            WebResource resource = getResource();
            if (resource.isDirectory()) {
                return CachedResource.buildInputStream(resource.getWebResourceRoot().list(this.webAppPath));
            }
            return getResource().getInputStream();
        }

        @Override // java.net.URLConnection
        public Permission getPermission() throws IOException {
            return this.resourceURL.openConnection().getPermission();
        }

        @Override // java.net.URLConnection
        public long getLastModified() {
            return getResource().getLastModified();
        }

        @Override // java.net.URLConnection
        public long getContentLengthLong() {
            return getResource().getContentLength();
        }

        private WebResource getResource() {
            return this.root.getResource(this.webAppPath, false, this.usesClassLoaderResources);
        }

        @Override // java.net.JarURLConnection
        public JarFile getJarFile() throws IOException {
            return ((JarURLConnection) this.resourceURL.openConnection()).getJarFile();
        }

        @Override // java.net.JarURLConnection
        public JarEntry getJarEntry() throws IOException {
            if (getEntryName() == null) {
                return null;
            }
            return super.getJarEntry();
        }
    }
}
