package org.springframework.boot;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/SpringBootVersion.class */
public final class SpringBootVersion {
    private SpringBootVersion() {
    }

    public static String getVersion() {
        return determineSpringBootVersion();
    }

    private static String determineSpringBootVersion() {
        String implementationVersion = SpringBootVersion.class.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            return implementationVersion;
        }
        CodeSource codeSource = SpringBootVersion.class.getProtectionDomain().getCodeSource();
        if (codeSource == null) {
            return null;
        }
        URL codeSourceLocation = codeSource.getLocation();
        try {
            URLConnection connection = codeSourceLocation.openConnection();
            if (connection instanceof JarURLConnection) {
                return getImplementationVersion(((JarURLConnection) connection).getJarFile());
            }
            JarFile jarFile = new JarFile(new File(codeSourceLocation.toURI()));
            String implementationVersion2 = getImplementationVersion(jarFile);
            if (jarFile != null) {
                if (0 != 0) {
                    jarFile.close();
                } else {
                    jarFile.close();
                }
            }
            return implementationVersion2;
        } catch (Exception e) {
            return null;
        }
    }

    private static String getImplementationVersion(JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }
}
