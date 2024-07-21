package org.apache.catalina.webresources;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import org.apache.catalina.webresources.AbstractArchiveResource;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.util.ResourceUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/webresources/JarWarResource.class */
public class JarWarResource extends AbstractArchiveResource {
    private static final Log log = LogFactory.getLog(JarWarResource.class);
    private final String archivePath;

    public JarWarResource(AbstractArchiveResourceSet archiveResourceSet, String webAppPath, String baseUrl, JarEntry jarEntry, String archivePath) {
        super(archiveResourceSet, webAppPath, "jar:war:" + baseUrl + UriUtil.getWarSeparator() + archivePath + ResourceUtils.JAR_URL_SEPARATOR, jarEntry, ResourceUtils.WAR_URL_PREFIX + baseUrl + UriUtil.getWarSeparator() + archivePath);
        this.archivePath = archivePath;
    }

    @Override // org.apache.catalina.webresources.AbstractArchiveResource
    protected AbstractArchiveResource.JarInputStreamWrapper getJarInputStreamWrapper() {
        JarFile warFile = null;
        JarInputStream jarIs = null;
        JarEntry entry = null;
        try {
            try {
                warFile = getArchiveResourceSet().openJarFile();
                JarEntry jarFileInWar = warFile.getJarEntry(this.archivePath);
                InputStream isInWar = warFile.getInputStream(jarFileInWar);
                jarIs = new JarInputStream(isInWar);
                entry = jarIs.getNextJarEntry();
                while (entry != null && !entry.getName().equals(getResource().getName())) {
                    entry = jarIs.getNextJarEntry();
                }
                if (entry == null) {
                    if (entry == null) {
                        if (jarIs != null) {
                            try {
                                jarIs.close();
                            } catch (IOException e) {
                            }
                        }
                        if (warFile != null) {
                            getArchiveResourceSet().closeJarFile();
                        }
                    }
                    return null;
                }
                AbstractArchiveResource.JarInputStreamWrapper jarInputStreamWrapper = new AbstractArchiveResource.JarInputStreamWrapper(entry, jarIs);
                if (entry == null) {
                    if (jarIs != null) {
                        try {
                            jarIs.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (warFile != null) {
                        getArchiveResourceSet().closeJarFile();
                    }
                }
                return jarInputStreamWrapper;
            } catch (Throwable th) {
                if (entry == null) {
                    if (jarIs != null) {
                        try {
                            jarIs.close();
                        } catch (IOException e3) {
                        }
                    }
                    if (warFile != null) {
                        getArchiveResourceSet().closeJarFile();
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("jarResource.getInputStreamFail", getResource().getName(), getBaseUrl()), e4);
            }
            if (0 == 0) {
                if (jarIs != null) {
                    try {
                        jarIs.close();
                    } catch (IOException e5) {
                    }
                }
                if (warFile != null) {
                    getArchiveResourceSet().closeJarFile();
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.catalina.webresources.AbstractResource
    public Log getLog() {
        return log;
    }
}
