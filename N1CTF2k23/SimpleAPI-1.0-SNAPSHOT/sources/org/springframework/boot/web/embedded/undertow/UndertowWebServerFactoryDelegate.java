package org.springframework.boot.web.embedded.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.tomcat.jni.Address;
import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.boot.web.server.Ssl;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowWebServerFactoryDelegate.class */
public class UndertowWebServerFactoryDelegate {
    private Integer bufferSize;
    private Integer ioThreads;
    private Integer workerThreads;
    private Boolean directBuffers;
    private File accessLogDirectory;
    private String accessLogPattern;
    private String accessLogPrefix;
    private String accessLogSuffix;
    private boolean useForwardHeaders;
    private Set<UndertowBuilderCustomizer> builderCustomizers = new LinkedHashSet();
    private boolean accessLogEnabled = false;
    private boolean accessLogRotate = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBuilderCustomizers(Collection<? extends UndertowBuilderCustomizer> customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.builderCustomizers = new LinkedHashSet(customizers);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addBuilderCustomizers(UndertowBuilderCustomizer... customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.builderCustomizers.addAll(Arrays.asList(customizers));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Collection<UndertowBuilderCustomizer> getBuilderCustomizers() {
        return this.builderCustomizers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIoThreads(Integer ioThreads) {
        this.ioThreads = ioThreads;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWorkerThreads(Integer workerThreads) {
        this.workerThreads = workerThreads;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUseDirectBuffers(Boolean directBuffers) {
        this.directBuffers = directBuffers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessLogDirectory(File accessLogDirectory) {
        this.accessLogDirectory = accessLogDirectory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessLogPattern(String accessLogPattern) {
        this.accessLogPattern = accessLogPattern;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessLogPrefix(String accessLogPrefix) {
        this.accessLogPrefix = accessLogPrefix;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getAccessLogPrefix() {
        return this.accessLogPrefix;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessLogSuffix(String accessLogSuffix) {
        this.accessLogSuffix = accessLogSuffix;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessLogEnabled(boolean accessLogEnabled) {
        this.accessLogEnabled = accessLogEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAccessLogEnabled() {
        return this.accessLogEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessLogRotate(boolean accessLogRotate) {
        this.accessLogRotate = accessLogRotate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUseForwardHeaders(boolean useForwardHeaders) {
        this.useForwardHeaders = useForwardHeaders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUseForwardHeaders() {
        return this.useForwardHeaders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Undertow.Builder createBuilder(AbstractConfigurableWebServerFactory factory) {
        Ssl ssl = factory.getSsl();
        InetAddress address = factory.getAddress();
        int port = factory.getPort();
        Undertow.Builder builder = Undertow.builder();
        if (this.bufferSize != null) {
            builder.setBufferSize(this.bufferSize.intValue());
        }
        if (this.ioThreads != null) {
            builder.setIoThreads(this.ioThreads.intValue());
        }
        if (this.workerThreads != null) {
            builder.setWorkerThreads(this.workerThreads.intValue());
        }
        if (this.directBuffers != null) {
            builder.setDirectBuffers(this.directBuffers.booleanValue());
        }
        if (ssl != null && ssl.isEnabled()) {
            new SslBuilderCustomizer(factory.getPort(), address, ssl, factory.getSslStoreProvider()).customize(builder);
            Http2 http2 = factory.getHttp2();
            if (http2 != null) {
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, Boolean.valueOf(http2.isEnabled()));
            }
        } else {
            builder.addHttpListener(port, address != null ? address.getHostAddress() : Address.APR_ANYADDR);
        }
        builder.setServerOption(UndertowOptions.SHUTDOWN_TIMEOUT, 0);
        for (UndertowBuilderCustomizer customizer : this.builderCustomizers) {
            customizer.customize(builder);
        }
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<HttpHandlerFactory> createHttpHandlerFactories(AbstractConfigurableWebServerFactory webServerFactory, HttpHandlerFactory... initialHttpHandlerFactories) {
        List<HttpHandlerFactory> factories = createHttpHandlerFactories(webServerFactory.getCompression(), this.useForwardHeaders, webServerFactory.getServerHeader(), webServerFactory.getShutdown(), initialHttpHandlerFactories);
        if (isAccessLogEnabled()) {
            factories.add(new AccessLogHttpHandlerFactory(this.accessLogDirectory, this.accessLogPattern, this.accessLogPrefix, this.accessLogSuffix, this.accessLogRotate));
        }
        return factories;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<HttpHandlerFactory> createHttpHandlerFactories(Compression compression, boolean useForwardHeaders, String serverHeader, Shutdown shutdown, HttpHandlerFactory... initialHttpHandlerFactories) {
        List<HttpHandlerFactory> factories = new ArrayList<>();
        factories.addAll(Arrays.asList(initialHttpHandlerFactories));
        if (compression != null && compression.getEnabled()) {
            factories.add(new CompressionHttpHandlerFactory(compression));
        }
        if (useForwardHeaders) {
            factories.add(Handlers::proxyPeerAddress);
        }
        if (StringUtils.hasText(serverHeader)) {
            factories.add(next -> {
                return Handlers.header(next, HttpHeaders.SERVER, serverHeader);
            });
        }
        if (shutdown == Shutdown.GRACEFUL) {
            factories.add(Handlers::gracefulShutdown);
        }
        return factories;
    }
}
