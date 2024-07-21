package org.springframework.boot.loader;

import org.springframework.boot.loader.archive.Archive;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/WarLauncher.class */
public class WarLauncher extends ExecutableArchiveLauncher {
    public WarLauncher() {
    }

    protected WarLauncher(Archive archive) {
        super(archive);
    }

    @Override // org.springframework.boot.loader.ExecutableArchiveLauncher
    protected boolean isPostProcessingClassPathArchives() {
        return false;
    }

    @Override // org.springframework.boot.loader.ExecutableArchiveLauncher
    protected boolean isSearchCandidate(Archive.Entry entry) {
        return entry.getName().startsWith("WEB-INF/");
    }

    @Override // org.springframework.boot.loader.ExecutableArchiveLauncher
    public boolean isNestedArchive(Archive.Entry entry) {
        if (entry.isDirectory()) {
            return entry.getName().equals("WEB-INF/classes/");
        }
        return entry.getName().startsWith("WEB-INF/lib/") || entry.getName().startsWith("WEB-INF/lib-provided/");
    }

    public static void main(String[] args) throws Exception {
        new WarLauncher().launch(args);
    }
}
