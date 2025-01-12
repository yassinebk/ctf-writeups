package org.springframework.boot.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.ExplodedArchive;
import org.springframework.boot.loader.archive.JarFileArchive;
import org.springframework.boot.loader.util.SystemPropertyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.support.WebContentGenerator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/PropertiesLauncher.class */
public class PropertiesLauncher extends Launcher {
    private static final String DEBUG = "loader.debug";
    public static final String MAIN = "loader.main";
    public static final String PATH = "loader.path";
    public static final String HOME = "loader.home";
    public static final String ARGS = "loader.args";
    public static final String CONFIG_NAME = "loader.config.name";
    public static final String CONFIG_LOCATION = "loader.config.location";
    public static final String SET_SYSTEM_PROPERTIES = "loader.system";
    private final File home;
    private List<String> paths = new ArrayList();
    private final Properties properties = new Properties();
    private final Archive parent;
    private volatile ClassPathArchives classPathArchives;
    private static final Class<?>[] PARENT_ONLY_PARAMS = {ClassLoader.class};
    private static final Class<?>[] URLS_AND_PARENT_PARAMS = {URL[].class, ClassLoader.class};
    private static final Class<?>[] NO_PARAMS = new Class[0];
    private static final URL[] NO_URLS = new URL[0];
    private static final Pattern WORD_SEPARATOR = Pattern.compile("\\W+");
    private static final String NESTED_ARCHIVE_SEPARATOR = "!" + File.separator;

    public PropertiesLauncher() {
        try {
            this.home = getHomeDirectory();
            initializeProperties();
            initializePaths();
            this.parent = createArchive();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    protected File getHomeDirectory() {
        try {
            return new File(getPropertyWithDefault(HOME, "${user.dir}"));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void initializeProperties() throws Exception {
        List<String> configs = new ArrayList<>();
        if (getProperty(CONFIG_LOCATION) != null) {
            configs.add(getProperty(CONFIG_LOCATION));
        } else {
            String[] names = getPropertyWithDefault(CONFIG_NAME, "loader").split(",");
            for (String name : names) {
                configs.add(ResourceUtils.FILE_URL_PREFIX + getHomeDirectory() + "/" + name + ".properties");
                configs.add("classpath:" + name + ".properties");
                configs.add("classpath:BOOT-INF/classes/" + name + ".properties");
            }
        }
        for (String config : configs) {
            InputStream resource = getResource(config);
            Throwable th = null;
            if (resource != null) {
                debug("Found: " + config);
                loadResource(resource);
                if (resource != null) {
                    if (0 != 0) {
                        try {
                            resource.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    resource.close();
                    return;
                }
                return;
            }
            try {
                debug("Not found: " + config);
                if (resource != null) {
                    if (0 != 0) {
                        try {
                            resource.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    } else {
                        resource.close();
                    }
                }
            } catch (Throwable th4) {
                throw th4;
            }
            try {
                throw th4;
            } catch (Throwable th5) {
                if (resource != null) {
                    if (th4 != null) {
                        try {
                            resource.close();
                        } catch (Throwable th6) {
                            th4.addSuppressed(th6);
                        }
                    } else {
                        resource.close();
                    }
                }
                throw th5;
            }
        }
    }

    private void loadResource(InputStream resource) throws Exception {
        this.properties.load(resource);
        Iterator it = Collections.list(this.properties.propertyNames()).iterator();
        while (it.hasNext()) {
            Object key = it.next();
            String text = this.properties.getProperty((String) key);
            String value = SystemPropertyUtils.resolvePlaceholders(this.properties, text);
            if (value != null) {
                this.properties.put(key, value);
            }
        }
        if ("true".equals(getProperty(SET_SYSTEM_PROPERTIES))) {
            debug("Adding resolved properties to System properties");
            Iterator it2 = Collections.list(this.properties.propertyNames()).iterator();
            while (it2.hasNext()) {
                Object key2 = it2.next();
                System.setProperty((String) key2, this.properties.getProperty((String) key2));
            }
        }
    }

    private InputStream getResource(String config) throws Exception {
        if (config.startsWith("classpath:")) {
            return getClasspathResource(config.substring("classpath:".length()));
        }
        String config2 = handleUrl(config);
        if (isUrl(config2)) {
            return getURLResource(config2);
        }
        return getFileResource(config2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String handleUrl(String path) throws UnsupportedEncodingException {
        if (path.startsWith("jar:file:") || path.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
            path = URLDecoder.decode(path, "UTF-8");
            if (path.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
                path = path.substring(ResourceUtils.FILE_URL_PREFIX.length());
                if (path.startsWith("//")) {
                    path = path.substring(2);
                }
            }
        }
        return path;
    }

    private boolean isUrl(String config) {
        return config.contains("://");
    }

    private InputStream getClasspathResource(String config) {
        while (config.startsWith("/")) {
            config = config.substring(1);
        }
        String config2 = "/" + config;
        debug("Trying classpath: " + config2);
        return getClass().getResourceAsStream(config2);
    }

    private InputStream getFileResource(String config) throws Exception {
        File file = new File(config);
        debug("Trying file: " + config);
        if (file.canRead()) {
            return new FileInputStream(file);
        }
        return null;
    }

    private InputStream getURLResource(String config) throws Exception {
        URL url = new URL(config);
        if (exists(url)) {
            URLConnection con = url.openConnection();
            try {
                return con.getInputStream();
            } catch (IOException ex) {
                if (con instanceof HttpURLConnection) {
                    ((HttpURLConnection) con).disconnect();
                }
                throw ex;
            }
        }
        return null;
    }

    private boolean exists(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        try {
            connection.setUseCaches(connection.getClass().getSimpleName().startsWith("JNLP"));
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod(WebContentGenerator.METHOD_HEAD);
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == 200) {
                    return true;
                }
                if (responseCode == 404) {
                    if (connection instanceof HttpURLConnection) {
                        ((HttpURLConnection) connection).disconnect();
                    }
                    return false;
                }
            }
            boolean z = connection.getContentLength() >= 0;
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            }
            return z;
        } finally {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            }
        }
    }

    private void initializePaths() throws Exception {
        String path = getProperty(PATH);
        if (path != null) {
            this.paths = parsePathsProperty(path);
        }
        debug("Nested archive paths: " + this.paths);
    }

    private List<String> parsePathsProperty(String commaSeparatedPaths) {
        List<String> paths = new ArrayList<>();
        for (String path : commaSeparatedPaths.split(",")) {
            String path2 = cleanupPath(path);
            paths.add((path2 == null || path2.isEmpty()) ? "/" : path2);
        }
        if (paths.isEmpty()) {
            paths.add("lib");
        }
        return paths;
    }

    protected String[] getArgs(String... args) throws Exception {
        String loaderArgs = getProperty(ARGS);
        if (loaderArgs != null) {
            String[] defaultArgs = loaderArgs.split("\\s+");
            args = new String[defaultArgs.length + args.length];
            System.arraycopy(defaultArgs, 0, args, 0, defaultArgs.length);
            System.arraycopy(args, 0, args, defaultArgs.length, args.length);
        }
        return args;
    }

    @Override // org.springframework.boot.loader.Launcher
    protected String getMainClass() throws Exception {
        String mainClass = getProperty(MAIN, "Start-Class");
        if (mainClass == null) {
            throw new IllegalStateException("No 'loader.main' or 'Start-Class' specified");
        }
        return mainClass;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.loader.Launcher
    public ClassLoader createClassLoader(Iterator<Archive> archives) throws Exception {
        String customLoaderClassName = getProperty("loader.classLoader");
        if (customLoaderClassName == null) {
            return super.createClassLoader(archives);
        }
        Set<URL> urls = new LinkedHashSet<>();
        while (archives.hasNext()) {
            urls.add(archives.next().getUrl());
        }
        ClassLoader loader = new LaunchedURLClassLoader((URL[]) urls.toArray(NO_URLS), getClass().getClassLoader());
        debug("Classpath for custom loader: " + urls);
        ClassLoader loader2 = wrapWithCustomClassLoader(loader, customLoaderClassName);
        debug("Using custom class loader: " + customLoaderClassName);
        return loader2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ClassLoader wrapWithCustomClassLoader(ClassLoader parent, String className) throws Exception {
        Class<?> cls = Class.forName(className, true, parent);
        ClassLoader classLoader = newClassLoader(cls, PARENT_ONLY_PARAMS, parent);
        if (classLoader == null) {
            classLoader = newClassLoader(cls, URLS_AND_PARENT_PARAMS, NO_URLS, parent);
        }
        if (classLoader == null) {
            classLoader = newClassLoader(cls, NO_PARAMS, new Object[0]);
        }
        if (classLoader == null) {
            throw new IllegalArgumentException("Unable to create class loader for " + className);
        }
        return classLoader;
    }

    private ClassLoader newClassLoader(Class<ClassLoader> loaderClass, Class<?>[] parameterTypes, Object... initargs) throws Exception {
        try {
            Constructor<ClassLoader> constructor = loaderClass.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(initargs);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String getProperty(String propertyKey) throws Exception {
        return getProperty(propertyKey, null, null);
    }

    private String getProperty(String propertyKey, String manifestKey) throws Exception {
        return getProperty(propertyKey, manifestKey, null);
    }

    private String getPropertyWithDefault(String propertyKey, String defaultValue) throws Exception {
        return getProperty(propertyKey, null, defaultValue);
    }

    private String getProperty(String propertyKey, String manifestKey, String defaultValue) throws Exception {
        String value;
        String value2;
        if (manifestKey == null) {
            manifestKey = toCamelCase(propertyKey.replace('.', '-'));
        }
        String property = SystemPropertyUtils.getProperty(propertyKey);
        if (property != null) {
            String value3 = SystemPropertyUtils.resolvePlaceholders(this.properties, property);
            debug("Property '" + propertyKey + "' from environment: " + value3);
            return value3;
        } else if (this.properties.containsKey(propertyKey)) {
            String value4 = SystemPropertyUtils.resolvePlaceholders(this.properties, this.properties.getProperty(propertyKey));
            debug("Property '" + propertyKey + "' from properties: " + value4);
            return value4;
        } else {
            try {
                if (this.home != null) {
                    ExplodedArchive archive = new ExplodedArchive(this.home, false);
                    Manifest manifest = archive.getManifest();
                    if (manifest != null && (value2 = manifest.getMainAttributes().getValue(manifestKey)) != null) {
                        debug("Property '" + manifestKey + "' from home directory manifest: " + value2);
                        String resolvePlaceholders = SystemPropertyUtils.resolvePlaceholders(this.properties, value2);
                        if (archive != null) {
                            if (0 != 0) {
                                archive.close();
                            } else {
                                archive.close();
                            }
                        }
                        return resolvePlaceholders;
                    } else if (archive != null) {
                        if (0 != 0) {
                            archive.close();
                        } else {
                            archive.close();
                        }
                    }
                }
            } catch (IllegalStateException e) {
            }
            Manifest manifest2 = createArchive().getManifest();
            if (manifest2 == null || (value = manifest2.getMainAttributes().getValue(manifestKey)) == null) {
                return defaultValue != null ? SystemPropertyUtils.resolvePlaceholders(this.properties, defaultValue) : defaultValue;
            }
            debug("Property '" + manifestKey + "' from archive manifest: " + value);
            return SystemPropertyUtils.resolvePlaceholders(this.properties, value);
        }
    }

    @Override // org.springframework.boot.loader.Launcher
    protected Iterator<Archive> getClassPathArchivesIterator() throws Exception {
        ClassPathArchives classPathArchives = this.classPathArchives;
        if (classPathArchives == null) {
            classPathArchives = new ClassPathArchives();
            this.classPathArchives = classPathArchives;
        }
        return classPathArchives.iterator();
    }

    public static void main(String[] args) throws Exception {
        PropertiesLauncher launcher = new PropertiesLauncher();
        launcher.launch(launcher.getArgs(args));
    }

    public static String toCamelCase(CharSequence string) {
        if (string == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        Matcher matcher = WORD_SEPARATOR.matcher(string);
        int i = 0;
        while (true) {
            int pos = i;
            if (matcher.find()) {
                builder.append(capitalize(string.subSequence(pos, matcher.end()).toString()));
                i = matcher.end();
            } else {
                builder.append(capitalize(string.subSequence(pos, string.length()).toString()));
                return builder.toString();
            }
        }
    }

    private static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debug(String message) {
        if (Boolean.getBoolean(DEBUG)) {
            System.out.println(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String cleanupPath(String path) {
        String path2 = path.trim();
        if (path2.startsWith("./")) {
            path2 = path2.substring(2);
        }
        String lowerCasePath = path2.toLowerCase(Locale.ENGLISH);
        if (lowerCasePath.endsWith(".jar") || lowerCasePath.endsWith(".zip")) {
            return path2;
        }
        if (path2.endsWith("/*")) {
            path2 = path2.substring(0, path2.length() - 1);
        } else if (!path2.endsWith("/") && !path2.equals(".")) {
            path2 = path2 + "/";
        }
        return path2;
    }

    void close() throws Exception {
        if (this.classPathArchives != null) {
            this.classPathArchives.close();
        }
        if (this.parent != null) {
            this.parent.close();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/PropertiesLauncher$ClassPathArchives.class */
    private class ClassPathArchives implements Iterable<Archive> {
        private final List<JarFileArchive> jarFileArchives = new ArrayList();
        private final List<Archive> classPathArchives = new ArrayList();

        ClassPathArchives() throws Exception {
            for (String path : PropertiesLauncher.this.paths) {
                for (Archive archive : getClassPathArchives(path)) {
                    addClassPathArchive(archive);
                }
            }
            addNestedEntries();
        }

        private void addClassPathArchive(Archive archive) throws IOException {
            if (!(archive instanceof ExplodedArchive)) {
                this.classPathArchives.add(archive);
                return;
            }
            this.classPathArchives.add(archive);
            this.classPathArchives.addAll(asList(archive.getNestedArchives(null, new ArchiveEntryFilter())));
        }

        private List<Archive> getClassPathArchives(String path) throws Exception {
            String root = PropertiesLauncher.this.cleanupPath(PropertiesLauncher.this.handleUrl(path));
            List<Archive> lib = new ArrayList<>();
            File file = new File(root);
            if (!"/".equals(root)) {
                if (!isAbsolutePath(root)) {
                    file = new File(PropertiesLauncher.this.home, root);
                }
                if (file.isDirectory()) {
                    PropertiesLauncher.this.debug("Adding classpath entries from " + file);
                    lib.add(new ExplodedArchive(file, false));
                }
            }
            Archive archive = getArchive(file);
            if (archive != null) {
                PropertiesLauncher.this.debug("Adding classpath entries from archive " + archive.getUrl() + root);
                lib.add(archive);
            }
            List<Archive> nestedArchives = getNestedArchives(root);
            if (nestedArchives != null) {
                PropertiesLauncher.this.debug("Adding classpath entries from nested " + root);
                lib.addAll(nestedArchives);
            }
            return lib;
        }

        private boolean isAbsolutePath(String root) {
            return root.contains(":") || root.startsWith("/");
        }

        private Archive getArchive(File file) throws IOException {
            if (isNestedArchivePath(file)) {
                return null;
            }
            String name = file.getName().toLowerCase(Locale.ENGLISH);
            if (name.endsWith(".jar") || name.endsWith(".zip")) {
                return getJarFileArchive(file);
            }
            return null;
        }

        private boolean isNestedArchivePath(File file) {
            return file.getPath().contains(PropertiesLauncher.NESTED_ARCHIVE_SEPARATOR);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private List<Archive> getNestedArchives(String path) throws Exception {
            Archive parent = PropertiesLauncher.this.parent;
            String root = path;
            if ((!root.equals("/") && root.startsWith("/")) || parent.getUrl().toURI().equals(PropertiesLauncher.this.home.toURI())) {
                return null;
            }
            int index = root.indexOf(33);
            if (index != -1) {
                File file = new File(PropertiesLauncher.this.home, root.substring(0, index));
                if (root.startsWith("jar:file:")) {
                    file = new File(root.substring("jar:file:".length(), index));
                }
                parent = getJarFileArchive(file);
                String substring = root.substring(index + 1);
                while (true) {
                    root = substring;
                    if (!root.startsWith("/")) {
                        break;
                    }
                    substring = root.substring(1);
                }
            }
            if (root.endsWith(".jar")) {
                File file2 = new File(PropertiesLauncher.this.home, root);
                if (file2.exists()) {
                    parent = getJarFileArchive(file2);
                    root = "";
                }
            }
            root = (root.equals("/") || root.equals("./") || root.equals(".")) ? "" : "";
            Archive.EntryFilter filter = new PrefixMatchingArchiveFilter(root);
            List<Archive> archives = asList(parent.getNestedArchives(null, filter));
            if ((root == null || root.isEmpty() || ".".equals(root)) && !path.endsWith(".jar") && parent != PropertiesLauncher.this.parent) {
                archives.add(parent);
            }
            return archives;
        }

        private void addNestedEntries() {
            try {
                Iterator<Archive> archives = PropertiesLauncher.this.parent.getNestedArchives(null, JarLauncher.NESTED_ARCHIVE_ENTRY_FILTER);
                while (archives.hasNext()) {
                    this.classPathArchives.add(archives.next());
                }
            } catch (IOException e) {
            }
        }

        private List<Archive> asList(Iterator<Archive> iterator) {
            List<Archive> list = new ArrayList<>();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            return list;
        }

        private JarFileArchive getJarFileArchive(File file) throws IOException {
            JarFileArchive archive = new JarFileArchive(file);
            this.jarFileArchives.add(archive);
            return archive;
        }

        @Override // java.lang.Iterable
        public Iterator<Archive> iterator() {
            return this.classPathArchives.iterator();
        }

        void close() throws IOException {
            for (JarFileArchive archive : this.jarFileArchives) {
                archive.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/PropertiesLauncher$PrefixMatchingArchiveFilter.class */
    public static final class PrefixMatchingArchiveFilter implements Archive.EntryFilter {
        private final String prefix;
        private final ArchiveEntryFilter filter;

        private PrefixMatchingArchiveFilter(String prefix) {
            this.filter = new ArchiveEntryFilter();
            this.prefix = prefix;
        }

        @Override // org.springframework.boot.loader.archive.Archive.EntryFilter
        public boolean matches(Archive.Entry entry) {
            if (entry.isDirectory()) {
                return entry.getName().equals(this.prefix);
            }
            return entry.getName().startsWith(this.prefix) && this.filter.matches(entry);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/PropertiesLauncher$ArchiveEntryFilter.class */
    public static final class ArchiveEntryFilter implements Archive.EntryFilter {
        private static final String DOT_JAR = ".jar";
        private static final String DOT_ZIP = ".zip";

        private ArchiveEntryFilter() {
        }

        @Override // org.springframework.boot.loader.archive.Archive.EntryFilter
        public boolean matches(Archive.Entry entry) {
            return entry.getName().endsWith(".jar") || entry.getName().endsWith(DOT_ZIP);
        }
    }
}
