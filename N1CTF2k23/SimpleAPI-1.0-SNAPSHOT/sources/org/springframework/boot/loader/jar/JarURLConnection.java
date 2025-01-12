package org.springframework.boot.loader.jar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.security.Permission;
import org.springframework.boot.loader.jar.AbstractJarFile;
import org.springframework.util.ResourceUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/JarURLConnection.class */
public final class JarURLConnection extends java.net.JarURLConnection {
    private static ThreadLocal<Boolean> useFastExceptions = new ThreadLocal<>();
    private static final FileNotFoundException FILE_NOT_FOUND_EXCEPTION = new FileNotFoundException("Jar file or entry not found");
    private static final IllegalStateException NOT_FOUND_CONNECTION_EXCEPTION = new IllegalStateException(FILE_NOT_FOUND_EXCEPTION);
    private static final String SEPARATOR = "!/";
    private static final URL EMPTY_JAR_URL;
    private static final JarEntryName EMPTY_JAR_ENTRY_NAME;
    private static final JarURLConnection NOT_FOUND_CONNECTION;
    private final AbstractJarFile jarFile;
    private Permission permission;
    private URL jarFileUrl;
    private final JarEntryName jarEntryName;
    private java.util.jar.JarEntry jarEntry;

    static {
        try {
            EMPTY_JAR_URL = new URL(ResourceUtils.JAR_URL_PREFIX, null, 0, "file:!/", new URLStreamHandler() { // from class: org.springframework.boot.loader.jar.JarURLConnection.1
                @Override // java.net.URLStreamHandler
                protected URLConnection openConnection(URL u) throws IOException {
                    return null;
                }
            });
            EMPTY_JAR_ENTRY_NAME = new JarEntryName(new StringSequence(""));
            NOT_FOUND_CONNECTION = notFound();
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private JarURLConnection(URL url, AbstractJarFile jarFile, JarEntryName jarEntryName) throws IOException {
        super(EMPTY_JAR_URL);
        this.url = url;
        this.jarFile = jarFile;
        this.jarEntryName = jarEntryName;
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        if (this.jarFile == null) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }
        if (!this.jarEntryName.isEmpty() && this.jarEntry == null) {
            this.jarEntry = this.jarFile.getJarEntry(getEntryName());
            if (this.jarEntry == null) {
                throwFileNotFound(this.jarEntryName, this.jarFile);
            }
        }
        this.connected = true;
    }

    @Override // java.net.JarURLConnection
    public java.util.jar.JarFile getJarFile() throws IOException {
        connect();
        return this.jarFile;
    }

    @Override // java.net.JarURLConnection
    public URL getJarFileURL() {
        if (this.jarFile == null) {
            throw NOT_FOUND_CONNECTION_EXCEPTION;
        }
        if (this.jarFileUrl == null) {
            this.jarFileUrl = buildJarFileUrl();
        }
        return this.jarFileUrl;
    }

    private URL buildJarFileUrl() {
        try {
            String spec = this.jarFile.getUrl().getFile();
            if (spec.endsWith("!/")) {
                spec = spec.substring(0, spec.length() - "!/".length());
            }
            if (!spec.contains("!/")) {
                return new URL(spec);
            }
            return new URL(ResourceUtils.JAR_URL_PREFIX + spec);
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override // java.net.JarURLConnection
    public java.util.jar.JarEntry getJarEntry() throws IOException {
        if (this.jarEntryName == null || this.jarEntryName.isEmpty()) {
            return null;
        }
        connect();
        return this.jarEntry;
    }

    @Override // java.net.JarURLConnection
    public String getEntryName() {
        if (this.jarFile == null) {
            throw NOT_FOUND_CONNECTION_EXCEPTION;
        }
        return this.jarEntryName.toString();
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        if (this.jarFile == null) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }
        if (this.jarEntryName.isEmpty() && this.jarFile.getType() == AbstractJarFile.JarFileType.DIRECT) {
            throw new IOException("no entry name specified");
        }
        connect();
        InputStream inputStream = this.jarEntryName.isEmpty() ? this.jarFile.getInputStream() : this.jarFile.getInputStream(this.jarEntry);
        if (inputStream == null) {
            throwFileNotFound(this.jarEntryName, this.jarFile);
        }
        return inputStream;
    }

    private void throwFileNotFound(Object entry, AbstractJarFile jarFile) throws FileNotFoundException {
        if (Boolean.TRUE.equals(useFastExceptions.get())) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }
        throw new FileNotFoundException("JAR entry " + entry + " not found in " + jarFile.getName());
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        long length = getContentLengthLong();
        if (length > 2147483647L) {
            return -1;
        }
        return (int) length;
    }

    @Override // java.net.URLConnection
    public long getContentLengthLong() {
        if (this.jarFile == null) {
            return -1L;
        }
        try {
            if (this.jarEntryName.isEmpty()) {
                return this.jarFile.size();
            }
            java.util.jar.JarEntry entry = getJarEntry();
            if (entry != null) {
                return (int) entry.getSize();
            }
            return -1L;
        } catch (IOException e) {
            return -1L;
        }
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        connect();
        return this.jarEntryName.isEmpty() ? this.jarFile : super.getContent();
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        if (this.jarEntryName != null) {
            return this.jarEntryName.getContentType();
        }
        return null;
    }

    @Override // java.net.URLConnection
    public Permission getPermission() throws IOException {
        if (this.jarFile == null) {
            throw FILE_NOT_FOUND_EXCEPTION;
        }
        if (this.permission == null) {
            this.permission = this.jarFile.getPermission();
        }
        return this.permission;
    }

    @Override // java.net.URLConnection
    public long getLastModified() {
        if (this.jarFile == null || this.jarEntryName.isEmpty()) {
            return 0L;
        }
        try {
            java.util.jar.JarEntry entry = getJarEntry();
            if (entry != null) {
                return entry.getTime();
            }
            return 0L;
        } catch (IOException e) {
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setUseFastExceptions(boolean useFastExceptions2) {
        useFastExceptions.set(Boolean.valueOf(useFastExceptions2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JarURLConnection get(URL url, JarFile jarFile) throws IOException {
        StringSequence spec = new StringSequence(url.getFile());
        int index = indexOfRootSpec(spec, jarFile.getPathFromRoot());
        if (index == -1) {
            return Boolean.TRUE.equals(useFastExceptions.get()) ? NOT_FOUND_CONNECTION : new JarURLConnection(url, null, EMPTY_JAR_ENTRY_NAME);
        }
        while (true) {
            int separator = spec.indexOf("!/", index);
            if (separator > 0) {
                JarEntryName entryName = JarEntryName.get(spec.subSequence(index, separator));
                JarEntry jarEntry = jarFile.getJarEntry(entryName.toCharSequence());
                if (jarEntry == null) {
                    return notFound(jarFile, entryName);
                }
                jarFile = jarFile.getNestedJarFile(jarEntry);
                index = separator + "!/".length();
            } else {
                JarEntryName jarEntryName = JarEntryName.get(spec, index);
                if (Boolean.TRUE.equals(useFastExceptions.get()) && !jarEntryName.isEmpty() && !jarFile.containsEntry(jarEntryName.toString())) {
                    return NOT_FOUND_CONNECTION;
                }
                return new JarURLConnection(url, jarFile.getWrapper(), jarEntryName);
            }
        }
    }

    private static int indexOfRootSpec(StringSequence file, String pathFromRoot) {
        int separatorIndex = file.indexOf("!/");
        if (separatorIndex < 0 || !file.startsWith(pathFromRoot, separatorIndex)) {
            return -1;
        }
        return separatorIndex + "!/".length() + pathFromRoot.length();
    }

    private static JarURLConnection notFound() {
        try {
            return notFound(null, null);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static JarURLConnection notFound(JarFile jarFile, JarEntryName jarEntryName) throws IOException {
        if (Boolean.TRUE.equals(useFastExceptions.get())) {
            return NOT_FOUND_CONNECTION;
        }
        return new JarURLConnection(null, jarFile, jarEntryName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/JarURLConnection$JarEntryName.class */
    public static class JarEntryName {
        private final StringSequence name;
        private String contentType;

        JarEntryName(StringSequence spec) {
            this.name = decode(spec);
        }

        private StringSequence decode(StringSequence source) {
            if (source.isEmpty() || source.indexOf('%') < 0) {
                return source;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length());
            write(source.toString(), bos);
            return new StringSequence(AsciiBytes.toString(bos.toByteArray()));
        }

        private void write(String source, ByteArrayOutputStream outputStream) {
            int length = source.length();
            int i = 0;
            while (i < length) {
                int c = source.charAt(i);
                if (c > 127) {
                    try {
                        String encoded = URLEncoder.encode(String.valueOf((char) c), "UTF-8");
                        write(encoded, outputStream);
                    } catch (UnsupportedEncodingException ex) {
                        throw new IllegalStateException(ex);
                    }
                } else {
                    if (c == 37) {
                        if (i + 2 >= length) {
                            throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                        }
                        c = decodeEscapeSequence(source, i);
                        i += 2;
                    }
                    outputStream.write(c);
                }
                i++;
            }
        }

        private char decodeEscapeSequence(String source, int i) {
            int hi = Character.digit(source.charAt(i + 1), 16);
            int lo = Character.digit(source.charAt(i + 2), 16);
            if (hi == -1 || lo == -1) {
                throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
            }
            return (char) ((hi << 4) + lo);
        }

        CharSequence toCharSequence() {
            return this.name;
        }

        public String toString() {
            return this.name.toString();
        }

        boolean isEmpty() {
            return this.name.isEmpty();
        }

        String getContentType() {
            if (this.contentType == null) {
                this.contentType = deduceContentType();
            }
            return this.contentType;
        }

        private String deduceContentType() {
            String type = isEmpty() ? "x-java/jar" : null;
            String type2 = type != null ? type : URLConnection.guessContentTypeFromName(toString());
            return type2 != null ? type2 : "content/unknown";
        }

        static JarEntryName get(StringSequence spec) {
            return get(spec, 0);
        }

        static JarEntryName get(StringSequence spec, int beginIndex) {
            if (spec.length() <= beginIndex) {
                return JarURLConnection.EMPTY_JAR_ENTRY_NAME;
            }
            return new JarEntryName(spec.subSequence(beginIndex));
        }
    }
}
