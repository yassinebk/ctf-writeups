package org.apache.catalina.session;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.Session;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/session/FileStore.class */
public final class FileStore extends StoreBase {
    private static final Log log = LogFactory.getLog(FileStore.class);
    private static final StringManager sm = StringManager.getManager(FileStore.class);
    private static final String FILE_EXT = ".session";
    private String directory = ".";
    private File directoryFile = null;
    private static final String storeName = "fileStore";
    private static final String threadName = "FileStore";

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String path) {
        String oldDirectory = this.directory;
        this.directory = path;
        this.directoryFile = null;
        this.support.firePropertyChange("directory", oldDirectory, this.directory);
    }

    public String getThreadName() {
        return threadName;
    }

    @Override // org.apache.catalina.session.StoreBase
    public String getStoreName() {
        return storeName;
    }

    @Override // org.apache.catalina.Store
    public int getSize() throws IOException {
        File dir = directory();
        if (dir == null) {
            return 0;
        }
        String[] files = dir.list();
        int keycount = 0;
        if (files != null) {
            for (String file : files) {
                if (file.endsWith(FILE_EXT)) {
                    keycount++;
                }
            }
        }
        return keycount;
    }

    @Override // org.apache.catalina.Store
    public void clear() throws IOException {
        String[] keys = keys();
        for (String key : keys) {
            remove(key);
        }
    }

    @Override // org.apache.catalina.Store
    public String[] keys() throws IOException {
        File dir = directory();
        if (dir == null) {
            return new String[0];
        }
        String[] files = dir.list();
        if (files == null || files.length < 1) {
            return new String[0];
        }
        List<String> list = new ArrayList<>();
        int n = FILE_EXT.length();
        for (String file : files) {
            if (file.endsWith(FILE_EXT)) {
                list.add(file.substring(0, file.length() - n));
            }
        }
        return (String[]) list.toArray(new String[0]);
    }

    /* JADX WARN: Not initialized variable reg: 14, insn: 0x013a: MOVE  (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('fis' java.io.FileInputStream)]), block:B:51:0x013a */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x013f: MOVE  (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:53:0x013f */
    @Override // org.apache.catalina.Store
    public Session load(String id) throws ClassNotFoundException, IOException {
        FileInputStream fis;
        Throwable th;
        File file = file(id);
        if (file == null || !file.exists()) {
            return null;
        }
        Context context = getManager().getContext();
        Log contextLog = context.getLogger();
        if (contextLog.isDebugEnabled()) {
            contextLog.debug(sm.getString(getStoreName() + ".loading", id, file.getAbsolutePath()));
        }
        ClassLoader oldThreadContextCL = context.bind(Globals.IS_SECURITY_ENABLED, null);
        try {
            try {
                try {
                    FileInputStream fis2 = new FileInputStream(file.getAbsolutePath());
                    Throwable th2 = null;
                    ObjectInputStream ois = getObjectInputStream(fis2);
                    Throwable th3 = null;
                    try {
                        StandardSession session = (StandardSession) this.manager.createEmptySession();
                        session.readObjectData(ois);
                        session.setManager(this.manager);
                        if (ois != null) {
                            if (0 != 0) {
                                try {
                                    ois.close();
                                } catch (Throwable th4) {
                                    th3.addSuppressed(th4);
                                }
                            } else {
                                ois.close();
                            }
                        }
                        if (fis2 != null) {
                            if (0 != 0) {
                                try {
                                    fis2.close();
                                } catch (Throwable th5) {
                                    th2.addSuppressed(th5);
                                }
                            } else {
                                fis2.close();
                            }
                        }
                        context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
                        return session;
                    } catch (Throwable th6) {
                        try {
                            throw th6;
                        } catch (Throwable th7) {
                            if (ois != null) {
                                if (th6 != null) {
                                    try {
                                        ois.close();
                                    } catch (Throwable th8) {
                                        th6.addSuppressed(th8);
                                    }
                                } else {
                                    ois.close();
                                }
                            }
                            throw th7;
                        }
                    }
                } catch (FileNotFoundException e) {
                    if (contextLog.isDebugEnabled()) {
                        contextLog.debug("No persisted data file found");
                    }
                    context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
                    return null;
                }
            } catch (Throwable th9) {
                if (fis != null) {
                    if (th != null) {
                        try {
                            fis.close();
                        } catch (Throwable th10) {
                            th.addSuppressed(th10);
                        }
                    } else {
                        fis.close();
                    }
                }
                throw th9;
            }
        } catch (Throwable th11) {
            context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
            throw th11;
        }
    }

    @Override // org.apache.catalina.Store
    public void remove(String id) throws IOException {
        File file = file(id);
        if (file == null) {
            return;
        }
        if (this.manager.getContext().getLogger().isDebugEnabled()) {
            this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".removing", id, file.getAbsolutePath()));
        }
        if (file.exists() && !file.delete()) {
            throw new IOException(sm.getString("fileStore.deleteSessionFailed", file));
        }
    }

    @Override // org.apache.catalina.Store
    public void save(Session session) throws IOException {
        File file = file(session.getIdInternal());
        if (file == null) {
            return;
        }
        if (this.manager.getContext().getLogger().isDebugEnabled()) {
            this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".saving", session.getIdInternal(), file.getAbsolutePath()));
        }
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
        Throwable th = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            ((StandardSession) session).writeObjectData(oos);
            if (oos != null) {
                if (0 != 0) {
                    oos.close();
                } else {
                    oos.close();
                }
            }
            if (fos != null) {
                if (0 == 0) {
                    fos.close();
                    return;
                }
                try {
                    fos.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (fos != null) {
                    if (th3 != null) {
                        try {
                            fos.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        fos.close();
                    }
                }
                throw th4;
            }
        }
    }

    private File directory() throws IOException {
        if (this.directory == null) {
            return null;
        }
        if (this.directoryFile != null) {
            return this.directoryFile;
        }
        File file = new File(this.directory);
        if (!file.isAbsolute()) {
            Context context = this.manager.getContext();
            ServletContext servletContext = context.getServletContext();
            File work = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            file = new File(work, this.directory);
        }
        if (!file.exists() || !file.isDirectory()) {
            if (!file.delete() && file.exists()) {
                throw new IOException(sm.getString("fileStore.deleteFailed", file));
            }
            if (!file.mkdirs() && !file.isDirectory()) {
                throw new IOException(sm.getString("fileStore.createFailed", file));
            }
        }
        this.directoryFile = file;
        return file;
    }

    private File file(String id) throws IOException {
        File storageDir = directory();
        if (storageDir == null) {
            return null;
        }
        String filename = id + FILE_EXT;
        File file = new File(storageDir, filename);
        if (!file.getCanonicalPath().startsWith(storageDir.getCanonicalPath())) {
            log.warn(sm.getString("fileStore.invalid", file.getPath(), id));
            return null;
        }
        return file;
    }
}
