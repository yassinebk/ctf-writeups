package org.springframework.boot.loader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Manifest;
import org.springframework.boot.loader.archive.Archive;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/ExecutableArchiveLauncher.class */
public abstract class ExecutableArchiveLauncher extends Launcher {
    private static final String START_CLASS_ATTRIBUTE = "Start-Class";
    protected static final String BOOT_CLASSPATH_INDEX_ATTRIBUTE = "Spring-Boot-Classpath-Index";
    private final Archive archive;
    private final ClassPathIndexFile classPathIndex;

    protected abstract boolean isNestedArchive(Archive.Entry entry);

    public ExecutableArchiveLauncher() {
        try {
            this.archive = createArchive();
            this.classPathIndex = getClassPathIndex(this.archive);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ExecutableArchiveLauncher(Archive archive) {
        try {
            this.archive = archive;
            this.classPathIndex = getClassPathIndex(this.archive);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassPathIndexFile getClassPathIndex(Archive archive) throws IOException {
        return null;
    }

    @Override // org.springframework.boot.loader.Launcher
    protected String getMainClass() throws Exception {
        Manifest manifest = this.archive.getManifest();
        String mainClass = null;
        if (manifest != null) {
            mainClass = manifest.getMainAttributes().getValue(START_CLASS_ATTRIBUTE);
        }
        if (mainClass == null) {
            throw new IllegalStateException("No 'Start-Class' manifest entry specified in " + this);
        }
        return mainClass;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.loader.Launcher
    public ClassLoader createClassLoader(Iterator<Archive> archives) throws Exception {
        List<URL> urls = new ArrayList<>(guessClassPathSize());
        while (archives.hasNext()) {
            urls.add(archives.next().getUrl());
        }
        if (this.classPathIndex != null) {
            urls.addAll(this.classPathIndex.getUrls());
        }
        return createClassLoader((URL[]) urls.toArray(new URL[0]));
    }

    private int guessClassPathSize() {
        if (this.classPathIndex != null) {
            return this.classPathIndex.size() + 10;
        }
        return 50;
    }

    @Override // org.springframework.boot.loader.Launcher
    protected Iterator<Archive> getClassPathArchivesIterator() throws Exception {
        Archive.EntryFilter searchFilter = this::isSearchCandidate;
        Iterator<Archive> archives = this.archive.getNestedArchives(searchFilter, entry -> {
            return isNestedArchive(entry) && !isEntryIndexed(entry);
        });
        if (isPostProcessingClassPathArchives()) {
            archives = applyClassPathArchivePostProcessing(archives);
        }
        return archives;
    }

    private boolean isEntryIndexed(Archive.Entry entry) {
        if (this.classPathIndex != null) {
            return this.classPathIndex.containsEntry(entry.getName());
        }
        return false;
    }

    private Iterator<Archive> applyClassPathArchivePostProcessing(Iterator<Archive> archives) throws Exception {
        List<Archive> list = new ArrayList<>();
        while (archives.hasNext()) {
            list.add(archives.next());
        }
        postProcessClassPathArchives(list);
        return list.iterator();
    }

    protected boolean isSearchCandidate(Archive.Entry entry) {
        return true;
    }

    protected boolean isPostProcessingClassPathArchives() {
        return true;
    }

    protected void postProcessClassPathArchives(List<Archive> archives) throws Exception {
    }

    @Override // org.springframework.boot.loader.Launcher
    protected boolean isExploded() {
        return this.archive.isExploded();
    }

    @Override // org.springframework.boot.loader.Launcher
    protected final Archive getArchive() {
        return this.archive;
    }
}
