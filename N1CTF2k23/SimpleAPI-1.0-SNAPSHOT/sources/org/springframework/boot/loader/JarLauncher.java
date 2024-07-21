package org.springframework.boot.loader;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.ExplodedArchive;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/JarLauncher.class */
public class JarLauncher extends ExecutableArchiveLauncher {
    private static final String DEFAULT_CLASSPATH_INDEX_LOCATION = "BOOT-INF/classpath.idx";
    static final Archive.EntryFilter NESTED_ARCHIVE_ENTRY_FILTER = entry -> {
        if (entry.isDirectory()) {
            return entry.getName().equals("BOOT-INF/classes/");
        }
        return entry.getName().startsWith("BOOT-INF/lib/");
    };

    public JarLauncher() {
    }

    protected JarLauncher(Archive archive) {
        super(archive);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.loader.ExecutableArchiveLauncher
    public ClassPathIndexFile getClassPathIndex(Archive archive) throws IOException {
        if (archive instanceof ExplodedArchive) {
            String location = getClassPathIndexFileLocation(archive);
            return ClassPathIndexFile.loadIfPossible(archive.getUrl(), location);
        }
        return super.getClassPathIndex(archive);
    }

    private String getClassPathIndexFileLocation(Archive archive) throws IOException {
        Manifest manifest = archive.getManifest();
        Attributes attributes = manifest != null ? manifest.getMainAttributes() : null;
        String location = attributes != null ? attributes.getValue("Spring-Boot-Classpath-Index") : null;
        return location != null ? location : DEFAULT_CLASSPATH_INDEX_LOCATION;
    }

    @Override // org.springframework.boot.loader.ExecutableArchiveLauncher
    protected boolean isPostProcessingClassPathArchives() {
        return false;
    }

    @Override // org.springframework.boot.loader.ExecutableArchiveLauncher
    protected boolean isSearchCandidate(Archive.Entry entry) {
        return entry.getName().startsWith("BOOT-INF/");
    }

    @Override // org.springframework.boot.loader.ExecutableArchiveLauncher
    protected boolean isNestedArchive(Archive.Entry entry) {
        return NESTED_ARCHIVE_ENTRY_FILTER.matches(entry);
    }

    public static void main(String[] args) throws Exception {
        new JarLauncher().launch(args);
    }
}
