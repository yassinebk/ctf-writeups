package org.springframework.boot.autoconfigure.web;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.catalina.valves.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.boot.web.servlet.server.Jsp;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties.class */
public class ServerProperties {
    private Integer port;
    private InetAddress address;
    private ForwardHeadersStrategy forwardHeadersStrategy;
    private String serverHeader;
    @NestedConfigurationProperty
    private Ssl ssl;
    @NestedConfigurationProperty
    private final ErrorProperties error = new ErrorProperties();
    private DataSize maxHttpHeaderSize = DataSize.ofKilobytes(8);
    private Shutdown shutdown = Shutdown.IMMEDIATE;
    @NestedConfigurationProperty
    private final Compression compression = new Compression();
    @NestedConfigurationProperty
    private final Http2 http2 = new Http2();
    private final Servlet servlet = new Servlet();
    private final Tomcat tomcat = new Tomcat();
    private final Jetty jetty = new Jetty();
    private final Netty netty = new Netty();
    private final Undertow undertow = new Undertow();

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$ForwardHeadersStrategy.class */
    public enum ForwardHeadersStrategy {
        NATIVE,
        FRAMEWORK,
        NONE
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public String getServerHeader() {
        return this.serverHeader;
    }

    public void setServerHeader(String serverHeader) {
        this.serverHeader = serverHeader;
    }

    public DataSize getMaxHttpHeaderSize() {
        return this.maxHttpHeaderSize;
    }

    public void setMaxHttpHeaderSize(DataSize maxHttpHeaderSize) {
        this.maxHttpHeaderSize = maxHttpHeaderSize;
    }

    public Shutdown getShutdown() {
        return this.shutdown;
    }

    public void setShutdown(Shutdown shutdown) {
        this.shutdown = shutdown;
    }

    public ErrorProperties getError() {
        return this.error;
    }

    public Ssl getSsl() {
        return this.ssl;
    }

    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    public Compression getCompression() {
        return this.compression;
    }

    public Http2 getHttp2() {
        return this.http2;
    }

    public Servlet getServlet() {
        return this.servlet;
    }

    public Tomcat getTomcat() {
        return this.tomcat;
    }

    public Jetty getJetty() {
        return this.jetty;
    }

    public Netty getNetty() {
        return this.netty;
    }

    public Undertow getUndertow() {
        return this.undertow;
    }

    public ForwardHeadersStrategy getForwardHeadersStrategy() {
        return this.forwardHeadersStrategy;
    }

    public void setForwardHeadersStrategy(ForwardHeadersStrategy forwardHeadersStrategy) {
        this.forwardHeadersStrategy = forwardHeadersStrategy;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Servlet.class */
    public static class Servlet {
        private String contextPath;
        private final Map<String, String> contextParameters = new HashMap();
        private String applicationDisplayName = "application";
        private boolean registerDefaultServlet = true;
        @NestedConfigurationProperty
        private final Encoding encoding = new Encoding();
        @NestedConfigurationProperty
        private final Jsp jsp = new Jsp();
        @NestedConfigurationProperty
        private final Session session = new Session();

        public String getContextPath() {
            return this.contextPath;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = cleanContextPath(contextPath);
        }

        private String cleanContextPath(String contextPath) {
            String candidate = StringUtils.trimWhitespace(contextPath);
            if (StringUtils.hasText(candidate) && candidate.endsWith("/")) {
                return candidate.substring(0, candidate.length() - 1);
            }
            return candidate;
        }

        public String getApplicationDisplayName() {
            return this.applicationDisplayName;
        }

        public void setApplicationDisplayName(String displayName) {
            this.applicationDisplayName = displayName;
        }

        public boolean isRegisterDefaultServlet() {
            return this.registerDefaultServlet;
        }

        public void setRegisterDefaultServlet(boolean registerDefaultServlet) {
            this.registerDefaultServlet = registerDefaultServlet;
        }

        public Map<String, String> getContextParameters() {
            return this.contextParameters;
        }

        public Encoding getEncoding() {
            return this.encoding;
        }

        public Jsp getJsp() {
            return this.jsp;
        }

        public Session getSession() {
            return this.session;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat.class */
    public static class Tomcat {
        private File basedir;
        private boolean useRelativeRedirects;
        private Duration connectionTimeout;
        private final Accesslog accesslog = new Accesslog();
        private final Threads threads = new Threads();
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration backgroundProcessorDelay = Duration.ofSeconds(10);
        private DataSize maxHttpFormPostSize = DataSize.ofMegabytes(2);
        private DataSize maxSwallowSize = DataSize.ofMegabytes(2);
        private Boolean redirectContextRoot = true;
        private Charset uriEncoding = StandardCharsets.UTF_8;
        private int maxConnections = 8192;
        private int acceptCount = 100;
        private int processorCache = 200;
        private List<String> additionalTldSkipPatterns = new ArrayList();
        private List<Character> relaxedPathChars = new ArrayList();
        private List<Character> relaxedQueryChars = new ArrayList();
        private final Resource resource = new Resource();
        private final Mbeanregistry mbeanregistry = new Mbeanregistry();
        private final Remoteip remoteip = new Remoteip();

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.threads.max")
        @Deprecated
        public int getMaxThreads() {
            return getThreads().getMax();
        }

        @Deprecated
        public void setMaxThreads(int maxThreads) {
            getThreads().setMax(maxThreads);
        }

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.threads.min-spare")
        @Deprecated
        public int getMinSpareThreads() {
            return getThreads().getMinSpare();
        }

        @Deprecated
        public void setMinSpareThreads(int minSpareThreads) {
            getThreads().setMinSpare(minSpareThreads);
        }

        public DataSize getMaxHttpFormPostSize() {
            return this.maxHttpFormPostSize;
        }

        public void setMaxHttpFormPostSize(DataSize maxHttpFormPostSize) {
            this.maxHttpFormPostSize = maxHttpFormPostSize;
        }

        public Accesslog getAccesslog() {
            return this.accesslog;
        }

        public Threads getThreads() {
            return this.threads;
        }

        public Duration getBackgroundProcessorDelay() {
            return this.backgroundProcessorDelay;
        }

        public void setBackgroundProcessorDelay(Duration backgroundProcessorDelay) {
            this.backgroundProcessorDelay = backgroundProcessorDelay;
        }

        public File getBasedir() {
            return this.basedir;
        }

        public void setBasedir(File basedir) {
            this.basedir = basedir;
        }

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.remoteip.internal-proxies")
        @Deprecated
        public String getInternalProxies() {
            return this.remoteip.getInternalProxies();
        }

        @Deprecated
        public void setInternalProxies(String internalProxies) {
            this.remoteip.setInternalProxies(internalProxies);
        }

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.remoteip.protocol-header")
        @Deprecated
        public String getProtocolHeader() {
            return this.remoteip.getProtocolHeader();
        }

        @Deprecated
        public void setProtocolHeader(String protocolHeader) {
            this.remoteip.setProtocolHeader(protocolHeader);
        }

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.remoteip.protocol-header-https-value")
        @Deprecated
        public String getProtocolHeaderHttpsValue() {
            return this.remoteip.getProtocolHeaderHttpsValue();
        }

        @Deprecated
        public void setProtocolHeaderHttpsValue(String protocolHeaderHttpsValue) {
            this.remoteip.setProtocolHeaderHttpsValue(protocolHeaderHttpsValue);
        }

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.remoteip.host-header")
        @Deprecated
        public String getHostHeader() {
            return this.remoteip.getHostHeader();
        }

        @Deprecated
        public void setHostHeader(String hostHeader) {
            this.remoteip.setHostHeader(hostHeader);
        }

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.remote.port-header")
        @Deprecated
        public String getPortHeader() {
            return this.remoteip.getPortHeader();
        }

        @Deprecated
        public void setPortHeader(String portHeader) {
            this.remoteip.setPortHeader(portHeader);
        }

        @DeprecatedConfigurationProperty(replacement = "server.tomcat.remoteip.remote-ip-header")
        @Deprecated
        public String getRemoteIpHeader() {
            return this.remoteip.getRemoteIpHeader();
        }

        @Deprecated
        public void setRemoteIpHeader(String remoteIpHeader) {
            this.remoteip.setRemoteIpHeader(remoteIpHeader);
        }

        public Boolean getRedirectContextRoot() {
            return this.redirectContextRoot;
        }

        public void setRedirectContextRoot(Boolean redirectContextRoot) {
            this.redirectContextRoot = redirectContextRoot;
        }

        @Deprecated
        public Boolean getUseRelativeRedirects() {
            return Boolean.valueOf(this.useRelativeRedirects);
        }

        public boolean isUseRelativeRedirects() {
            return this.useRelativeRedirects;
        }

        public void setUseRelativeRedirects(boolean useRelativeRedirects) {
            this.useRelativeRedirects = useRelativeRedirects;
        }

        @Deprecated
        public void setUseRelativeRedirects(Boolean useRelativeRedirects) {
            this.useRelativeRedirects = useRelativeRedirects != null ? useRelativeRedirects.booleanValue() : false;
        }

        public Charset getUriEncoding() {
            return this.uriEncoding;
        }

        public void setUriEncoding(Charset uriEncoding) {
            this.uriEncoding = uriEncoding;
        }

        public int getMaxConnections() {
            return this.maxConnections;
        }

        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }

        public DataSize getMaxSwallowSize() {
            return this.maxSwallowSize;
        }

        public void setMaxSwallowSize(DataSize maxSwallowSize) {
            this.maxSwallowSize = maxSwallowSize;
        }

        public int getAcceptCount() {
            return this.acceptCount;
        }

        public void setAcceptCount(int acceptCount) {
            this.acceptCount = acceptCount;
        }

        public int getProcessorCache() {
            return this.processorCache;
        }

        public void setProcessorCache(int processorCache) {
            this.processorCache = processorCache;
        }

        public List<String> getAdditionalTldSkipPatterns() {
            return this.additionalTldSkipPatterns;
        }

        public void setAdditionalTldSkipPatterns(List<String> additionalTldSkipPatterns) {
            this.additionalTldSkipPatterns = additionalTldSkipPatterns;
        }

        public List<Character> getRelaxedPathChars() {
            return this.relaxedPathChars;
        }

        public void setRelaxedPathChars(List<Character> relaxedPathChars) {
            this.relaxedPathChars = relaxedPathChars;
        }

        public List<Character> getRelaxedQueryChars() {
            return this.relaxedQueryChars;
        }

        public void setRelaxedQueryChars(List<Character> relaxedQueryChars) {
            this.relaxedQueryChars = relaxedQueryChars;
        }

        public Duration getConnectionTimeout() {
            return this.connectionTimeout;
        }

        public void setConnectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public Resource getResource() {
            return this.resource;
        }

        public Mbeanregistry getMbeanregistry() {
            return this.mbeanregistry;
        }

        public Remoteip getRemoteip() {
            return this.remoteip;
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat$Accesslog.class */
        public static class Accesslog {
            private String conditionIf;
            private String conditionUnless;
            private String encoding;
            private String locale;
            private boolean enabled = false;
            private String pattern = Constants.AccessLog.COMMON_ALIAS;
            private String directory = "logs";
            protected String prefix = "access_log";
            private String suffix = ".log";
            private boolean checkExists = false;
            private boolean rotate = true;
            private boolean renameOnRotate = false;
            private int maxDays = -1;
            private String fileDateFormat = ".yyyy-MM-dd";
            private boolean ipv6Canonical = false;
            private boolean requestAttributesEnabled = false;
            private boolean buffered = true;

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getConditionIf() {
                return this.conditionIf;
            }

            public void setConditionIf(String conditionIf) {
                this.conditionIf = conditionIf;
            }

            public String getConditionUnless() {
                return this.conditionUnless;
            }

            public void setConditionUnless(String conditionUnless) {
                this.conditionUnless = conditionUnless;
            }

            public String getPattern() {
                return this.pattern;
            }

            public void setPattern(String pattern) {
                this.pattern = pattern;
            }

            public String getDirectory() {
                return this.directory;
            }

            public void setDirectory(String directory) {
                this.directory = directory;
            }

            public String getPrefix() {
                return this.prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }

            public String getSuffix() {
                return this.suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
            }

            public String getEncoding() {
                return this.encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public String getLocale() {
                return this.locale;
            }

            public void setLocale(String locale) {
                this.locale = locale;
            }

            public boolean isCheckExists() {
                return this.checkExists;
            }

            public void setCheckExists(boolean checkExists) {
                this.checkExists = checkExists;
            }

            public boolean isRotate() {
                return this.rotate;
            }

            public void setRotate(boolean rotate) {
                this.rotate = rotate;
            }

            public boolean isRenameOnRotate() {
                return this.renameOnRotate;
            }

            public void setRenameOnRotate(boolean renameOnRotate) {
                this.renameOnRotate = renameOnRotate;
            }

            public int getMaxDays() {
                return this.maxDays;
            }

            public void setMaxDays(int maxDays) {
                this.maxDays = maxDays;
            }

            public String getFileDateFormat() {
                return this.fileDateFormat;
            }

            public void setFileDateFormat(String fileDateFormat) {
                this.fileDateFormat = fileDateFormat;
            }

            public boolean isIpv6Canonical() {
                return this.ipv6Canonical;
            }

            public void setIpv6Canonical(boolean ipv6Canonical) {
                this.ipv6Canonical = ipv6Canonical;
            }

            public boolean isRequestAttributesEnabled() {
                return this.requestAttributesEnabled;
            }

            public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
                this.requestAttributesEnabled = requestAttributesEnabled;
            }

            public boolean isBuffered() {
                return this.buffered;
            }

            public void setBuffered(boolean buffered) {
                this.buffered = buffered;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat$Threads.class */
        public static class Threads {
            private int max = 200;
            private int minSpare = 10;

            public int getMax() {
                return this.max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public int getMinSpare() {
                return this.minSpare;
            }

            public void setMinSpare(int minSpare) {
                this.minSpare = minSpare;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat$Resource.class */
        public static class Resource {
            private boolean allowCaching = true;
            private Duration cacheTtl;

            public boolean isAllowCaching() {
                return this.allowCaching;
            }

            public void setAllowCaching(boolean allowCaching) {
                this.allowCaching = allowCaching;
            }

            public Duration getCacheTtl() {
                return this.cacheTtl;
            }

            public void setCacheTtl(Duration cacheTtl) {
                this.cacheTtl = cacheTtl;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat$Mbeanregistry.class */
        public static class Mbeanregistry {
            private boolean enabled;

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Tomcat$Remoteip.class */
        public static class Remoteip {
            private String protocolHeader;
            private String remoteIpHeader;
            private String internalProxies = "10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|192\\.168\\.\\d{1,3}\\.\\d{1,3}|169\\.254\\.\\d{1,3}\\.\\d{1,3}|127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}|0:0:0:0:0:0:0:1|::1";
            private String protocolHeaderHttpsValue = "https";
            private String hostHeader = "X-Forwarded-Host";
            private String portHeader = "X-Forwarded-Port";

            public String getInternalProxies() {
                return this.internalProxies;
            }

            public void setInternalProxies(String internalProxies) {
                this.internalProxies = internalProxies;
            }

            public String getProtocolHeader() {
                return this.protocolHeader;
            }

            public void setProtocolHeader(String protocolHeader) {
                this.protocolHeader = protocolHeader;
            }

            public String getProtocolHeaderHttpsValue() {
                return this.protocolHeaderHttpsValue;
            }

            public String getHostHeader() {
                return this.hostHeader;
            }

            public void setHostHeader(String hostHeader) {
                this.hostHeader = hostHeader;
            }

            public void setProtocolHeaderHttpsValue(String protocolHeaderHttpsValue) {
                this.protocolHeaderHttpsValue = protocolHeaderHttpsValue;
            }

            public String getPortHeader() {
                return this.portHeader;
            }

            public void setPortHeader(String portHeader) {
                this.portHeader = portHeader;
            }

            public String getRemoteIpHeader() {
                return this.remoteIpHeader;
            }

            public void setRemoteIpHeader(String remoteIpHeader) {
                this.remoteIpHeader = remoteIpHeader;
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Jetty.class */
    public static class Jetty {
        private final Accesslog accesslog = new Accesslog();
        private final Threads threads = new Threads();
        private DataSize maxHttpFormPostSize = DataSize.ofBytes(200000);
        private Duration connectionIdleTimeout;

        public Accesslog getAccesslog() {
            return this.accesslog;
        }

        public Threads getThreads() {
            return this.threads;
        }

        public DataSize getMaxHttpFormPostSize() {
            return this.maxHttpFormPostSize;
        }

        public void setMaxHttpFormPostSize(DataSize maxHttpFormPostSize) {
            this.maxHttpFormPostSize = maxHttpFormPostSize;
        }

        @DeprecatedConfigurationProperty(replacement = "server.jetty.threads.acceptors")
        @Deprecated
        public Integer getAcceptors() {
            return getThreads().getAcceptors();
        }

        @Deprecated
        public void setAcceptors(Integer acceptors) {
            getThreads().setAcceptors(acceptors);
        }

        @DeprecatedConfigurationProperty(replacement = "server.jetty.threads.selectors")
        @Deprecated
        public Integer getSelectors() {
            return getThreads().getSelectors();
        }

        @Deprecated
        public void setSelectors(Integer selectors) {
            getThreads().setSelectors(selectors);
        }

        @DeprecatedConfigurationProperty(replacement = "server.jetty.threads.min")
        @Deprecated
        public Integer getMinThreads() {
            return getThreads().getMin();
        }

        @Deprecated
        public void setMinThreads(Integer minThreads) {
            getThreads().setMin(minThreads);
        }

        @DeprecatedConfigurationProperty(replacement = "server.jetty.threads.max")
        @Deprecated
        public Integer getMaxThreads() {
            return getThreads().getMax();
        }

        @Deprecated
        public void setMaxThreads(Integer maxThreads) {
            getThreads().setMax(maxThreads);
        }

        @DeprecatedConfigurationProperty(replacement = "server.jetty.threads.max-queue-capacity")
        @Deprecated
        public Integer getMaxQueueCapacity() {
            return getThreads().getMaxQueueCapacity();
        }

        @Deprecated
        public void setMaxQueueCapacity(Integer maxQueueCapacity) {
            getThreads().setMaxQueueCapacity(maxQueueCapacity);
        }

        @DeprecatedConfigurationProperty(replacement = "server.jetty.threads.idle-timeout")
        @Deprecated
        public Duration getThreadIdleTimeout() {
            return getThreads().getIdleTimeout();
        }

        @Deprecated
        public void setThreadIdleTimeout(Duration threadIdleTimeout) {
            getThreads().setIdleTimeout(threadIdleTimeout);
        }

        public Duration getConnectionIdleTimeout() {
            return this.connectionIdleTimeout;
        }

        public void setConnectionIdleTimeout(Duration connectionIdleTimeout) {
            this.connectionIdleTimeout = connectionIdleTimeout;
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Jetty$Accesslog.class */
        public static class Accesslog {
            private String customFormat;
            private String filename;
            private String fileDateFormat;
            private boolean append;
            private List<String> ignorePaths;
            private boolean enabled = false;
            private FORMAT format = FORMAT.NCSA;
            private int retentionPeriod = 31;

            /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Jetty$Accesslog$FORMAT.class */
            public enum FORMAT {
                NCSA,
                EXTENDED_NCSA
            }

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public FORMAT getFormat() {
                return this.format;
            }

            public void setFormat(FORMAT format) {
                this.format = format;
            }

            public String getCustomFormat() {
                return this.customFormat;
            }

            public void setCustomFormat(String customFormat) {
                this.customFormat = customFormat;
            }

            public String getFilename() {
                return this.filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getFileDateFormat() {
                return this.fileDateFormat;
            }

            public void setFileDateFormat(String fileDateFormat) {
                this.fileDateFormat = fileDateFormat;
            }

            public int getRetentionPeriod() {
                return this.retentionPeriod;
            }

            public void setRetentionPeriod(int retentionPeriod) {
                this.retentionPeriod = retentionPeriod;
            }

            public boolean isAppend() {
                return this.append;
            }

            public void setAppend(boolean append) {
                this.append = append;
            }

            public List<String> getIgnorePaths() {
                return this.ignorePaths;
            }

            public void setIgnorePaths(List<String> ignorePaths) {
                this.ignorePaths = ignorePaths;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Jetty$Threads.class */
        public static class Threads {
            private Integer maxQueueCapacity;
            private Integer acceptors = -1;
            private Integer selectors = -1;
            private Integer max = 200;
            private Integer min = 8;
            private Duration idleTimeout = Duration.ofMillis(60000);

            public Integer getAcceptors() {
                return this.acceptors;
            }

            public void setAcceptors(Integer acceptors) {
                this.acceptors = acceptors;
            }

            public Integer getSelectors() {
                return this.selectors;
            }

            public void setSelectors(Integer selectors) {
                this.selectors = selectors;
            }

            public void setMin(Integer min) {
                this.min = min;
            }

            public Integer getMin() {
                return this.min;
            }

            public void setMax(Integer max) {
                this.max = max;
            }

            public Integer getMax() {
                return this.max;
            }

            public Integer getMaxQueueCapacity() {
                return this.maxQueueCapacity;
            }

            public void setMaxQueueCapacity(Integer maxQueueCapacity) {
                this.maxQueueCapacity = maxQueueCapacity;
            }

            public void setIdleTimeout(Duration idleTimeout) {
                this.idleTimeout = idleTimeout;
            }

            public Duration getIdleTimeout() {
                return this.idleTimeout;
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Netty.class */
    public static class Netty {
        private Duration connectionTimeout;

        public Duration getConnectionTimeout() {
            return this.connectionTimeout;
        }

        public void setConnectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Undertow.class */
    public static class Undertow {
        private DataSize bufferSize;
        private Boolean directBuffers;
        private Duration noRequestTimeout;
        private DataSize maxHttpPostSize = DataSize.ofBytes(-1);
        private boolean eagerFilterInit = true;
        private int maxParameters = 1000;
        private int maxHeaders = 200;
        private int maxCookies = 200;
        private boolean allowEncodedSlash = false;
        private boolean decodeUrl = true;
        private Charset urlCharset = StandardCharsets.UTF_8;
        private boolean alwaysSetKeepAlive = true;
        private final Accesslog accesslog = new Accesslog();
        private final Threads threads = new Threads();
        private final Options options = new Options();

        public DataSize getMaxHttpPostSize() {
            return this.maxHttpPostSize;
        }

        public void setMaxHttpPostSize(DataSize maxHttpPostSize) {
            this.maxHttpPostSize = maxHttpPostSize;
        }

        public DataSize getBufferSize() {
            return this.bufferSize;
        }

        public void setBufferSize(DataSize bufferSize) {
            this.bufferSize = bufferSize;
        }

        @DeprecatedConfigurationProperty(replacement = "server.undertow.threads.io")
        @Deprecated
        public Integer getIoThreads() {
            return getThreads().getIo();
        }

        @Deprecated
        public void setIoThreads(Integer ioThreads) {
            getThreads().setIo(ioThreads);
        }

        @DeprecatedConfigurationProperty(replacement = "server.undertow.threads.worker")
        @Deprecated
        public Integer getWorkerThreads() {
            return getThreads().getWorker();
        }

        @Deprecated
        public void setWorkerThreads(Integer workerThreads) {
            getThreads().setWorker(workerThreads);
        }

        public Boolean getDirectBuffers() {
            return this.directBuffers;
        }

        public void setDirectBuffers(Boolean directBuffers) {
            this.directBuffers = directBuffers;
        }

        public boolean isEagerFilterInit() {
            return this.eagerFilterInit;
        }

        public void setEagerFilterInit(boolean eagerFilterInit) {
            this.eagerFilterInit = eagerFilterInit;
        }

        public int getMaxParameters() {
            return this.maxParameters;
        }

        public void setMaxParameters(Integer maxParameters) {
            this.maxParameters = maxParameters.intValue();
        }

        public int getMaxHeaders() {
            return this.maxHeaders;
        }

        public void setMaxHeaders(int maxHeaders) {
            this.maxHeaders = maxHeaders;
        }

        public Integer getMaxCookies() {
            return Integer.valueOf(this.maxCookies);
        }

        public void setMaxCookies(Integer maxCookies) {
            this.maxCookies = maxCookies.intValue();
        }

        public boolean isAllowEncodedSlash() {
            return this.allowEncodedSlash;
        }

        public void setAllowEncodedSlash(boolean allowEncodedSlash) {
            this.allowEncodedSlash = allowEncodedSlash;
        }

        public boolean isDecodeUrl() {
            return this.decodeUrl;
        }

        public void setDecodeUrl(Boolean decodeUrl) {
            this.decodeUrl = decodeUrl.booleanValue();
        }

        public Charset getUrlCharset() {
            return this.urlCharset;
        }

        public void setUrlCharset(Charset urlCharset) {
            this.urlCharset = urlCharset;
        }

        public boolean isAlwaysSetKeepAlive() {
            return this.alwaysSetKeepAlive;
        }

        public void setAlwaysSetKeepAlive(boolean alwaysSetKeepAlive) {
            this.alwaysSetKeepAlive = alwaysSetKeepAlive;
        }

        public Duration getNoRequestTimeout() {
            return this.noRequestTimeout;
        }

        public void setNoRequestTimeout(Duration noRequestTimeout) {
            this.noRequestTimeout = noRequestTimeout;
        }

        public Accesslog getAccesslog() {
            return this.accesslog;
        }

        public Threads getThreads() {
            return this.threads;
        }

        public Options getOptions() {
            return this.options;
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Undertow$Accesslog.class */
        public static class Accesslog {
            private boolean enabled = false;
            private String pattern = Constants.AccessLog.COMMON_ALIAS;
            protected String prefix = "access_log.";
            private String suffix = "log";
            private File dir = new File("logs");
            private boolean rotate = true;

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getPattern() {
                return this.pattern;
            }

            public void setPattern(String pattern) {
                this.pattern = pattern;
            }

            public String getPrefix() {
                return this.prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }

            public String getSuffix() {
                return this.suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
            }

            public File getDir() {
                return this.dir;
            }

            public void setDir(File dir) {
                this.dir = dir;
            }

            public boolean isRotate() {
                return this.rotate;
            }

            public void setRotate(boolean rotate) {
                this.rotate = rotate;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Undertow$Threads.class */
        public static class Threads {
            private Integer io;
            private Integer worker;

            public Integer getIo() {
                return this.io;
            }

            public void setIo(Integer io) {
                this.io = io;
            }

            public Integer getWorker() {
                return this.worker;
            }

            public void setWorker(Integer worker) {
                this.worker = worker;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerProperties$Undertow$Options.class */
        public static class Options {
            private Map<String, String> socket = new LinkedHashMap();
            private Map<String, String> server = new LinkedHashMap();

            public Map<String, String> getServer() {
                return this.server;
            }

            public Map<String, String> getSocket() {
                return this.socket;
            }
        }
    }
}
