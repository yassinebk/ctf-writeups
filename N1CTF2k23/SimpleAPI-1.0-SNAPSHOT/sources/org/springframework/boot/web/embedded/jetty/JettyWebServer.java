package org.springframework.boot.web.embedded.jetty;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;
import org.springframework.boot.web.server.PortInUseException;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/JettyWebServer.class */
public class JettyWebServer implements WebServer {
    private static final Log logger = LogFactory.getLog(JettyWebServer.class);
    private final Object monitor;
    private final Server server;
    private final boolean autoStart;
    private final GracefulShutdown gracefulShutdown;
    private Connector[] connectors;
    private volatile boolean started;

    public JettyWebServer(Server server) {
        this(server, true);
    }

    public JettyWebServer(Server server, boolean autoStart) {
        this.monitor = new Object();
        this.autoStart = autoStart;
        Assert.notNull(server, "Jetty Server must not be null");
        this.server = server;
        this.gracefulShutdown = createGracefulShutdown(server);
        initialize();
    }

    private GracefulShutdown createGracefulShutdown(Server server) {
        StatisticsHandler statisticsHandler = findStatisticsHandler(server);
        if (statisticsHandler == null) {
            return null;
        }
        statisticsHandler.getClass();
        return new GracefulShutdown(server, this::getRequestsActive);
    }

    private StatisticsHandler findStatisticsHandler(Server server) {
        return findStatisticsHandler(server.getHandler());
    }

    private StatisticsHandler findStatisticsHandler(Handler handler) {
        if (handler instanceof StatisticsHandler) {
            return (StatisticsHandler) handler;
        }
        if (handler instanceof HandlerWrapper) {
            return findStatisticsHandler(((HandlerWrapper) handler).getHandler());
        }
        return null;
    }

    private void initialize() {
        synchronized (this.monitor) {
            this.connectors = this.server.getConnectors();
            this.server.addBean(new AbstractLifeCycle() { // from class: org.springframework.boot.web.embedded.jetty.JettyWebServer.1
                protected void doStart() throws Exception {
                    Connector[] connectorArr;
                    for (Connector connector : JettyWebServer.this.connectors) {
                        Assert.state(connector.isStopped(), () -> {
                            return "Connector " + connector + " has been started prematurely";
                        });
                    }
                    JettyWebServer.this.server.setConnectors((Connector[]) null);
                }
            });
            this.server.start();
            this.server.setStopAtShutdown(false);
        }
    }

    private void stopSilently() {
        try {
            this.server.stop();
        } catch (Exception e) {
        }
    }

    @Override // org.springframework.boot.web.server.WebServer
    public void start() throws WebServerException {
        Handler[] handlers;
        synchronized (this.monitor) {
            if (this.started) {
                return;
            }
            this.server.setConnectors(this.connectors);
            if (this.autoStart) {
                try {
                    this.server.start();
                    for (Handler handler : this.server.getHandlers()) {
                        handleDeferredInitialize(handler);
                    }
                    Connector[] connectors = this.server.getConnectors();
                    for (Connector connector : connectors) {
                        try {
                            connector.start();
                        } catch (IOException ex) {
                            if (connector instanceof NetworkConnector) {
                                PortInUseException.throwIfPortBindingException(ex, () -> {
                                    return ((NetworkConnector) connector).getPort();
                                });
                            }
                            throw ex;
                        }
                    }
                    this.started = true;
                    logger.info("Jetty started on port(s) " + getActualPortsDescription() + " with context path '" + getContextPath() + "'");
                } catch (WebServerException ex2) {
                    stopSilently();
                    throw ex2;
                } catch (Exception ex3) {
                    stopSilently();
                    throw new WebServerException("Unable to start embedded Jetty server", ex3);
                }
            }
        }
    }

    private String getActualPortsDescription() {
        Connector[] connectors;
        StringBuilder ports = new StringBuilder();
        for (Connector connector : this.server.getConnectors()) {
            if (ports.length() != 0) {
                ports.append(", ");
            }
            ports.append(getLocalPort(connector)).append(getProtocols(connector));
        }
        return ports.toString();
    }

    private Integer getLocalPort(Connector connector) {
        try {
            return (Integer) ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(connector.getClass(), "getLocalPort"), connector);
        } catch (Exception ex) {
            logger.info("could not determine port ( " + ex.getMessage() + ")");
            return 0;
        }
    }

    private String getProtocols(Connector connector) {
        List<String> protocols = connector.getProtocols();
        return " (" + StringUtils.collectionToDelimitedString(protocols, ", ") + ")";
    }

    private String getContextPath() {
        return (String) Arrays.stream(this.server.getHandlers()).map(this::findContextHandler).filter((v0) -> {
            return Objects.nonNull(v0);
        }).map((v0) -> {
            return v0.getContextPath();
        }).collect(Collectors.joining(" "));
    }

    private ContextHandler findContextHandler(Handler handler) {
        while (handler instanceof HandlerWrapper) {
            if (handler instanceof ContextHandler) {
                return (ContextHandler) handler;
            }
            handler = ((HandlerWrapper) handler).getHandler();
        }
        return null;
    }

    private void handleDeferredInitialize(Handler... handlers) throws Exception {
        for (Handler handler : handlers) {
            if (handler instanceof JettyEmbeddedWebAppContext) {
                ((JettyEmbeddedWebAppContext) handler).deferredInitialize();
            } else if (handler instanceof HandlerWrapper) {
                handleDeferredInitialize(((HandlerWrapper) handler).getHandler());
            } else if (handler instanceof HandlerCollection) {
                handleDeferredInitialize(((HandlerCollection) handler).getHandlers());
            }
        }
    }

    @Override // org.springframework.boot.web.server.WebServer
    public void stop() {
        synchronized (this.monitor) {
            this.started = false;
            if (this.gracefulShutdown != null) {
                this.gracefulShutdown.abort();
            }
            try {
                this.server.stop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                throw new WebServerException("Unable to stop embedded Jetty server", ex);
            }
        }
    }

    @Override // org.springframework.boot.web.server.WebServer
    public int getPort() {
        Connector[] connectors = this.server.getConnectors();
        if (0 < connectors.length) {
            Connector connector = connectors[0];
            return getLocalPort(connector).intValue();
        }
        return 0;
    }

    @Override // org.springframework.boot.web.server.WebServer
    public void shutDownGracefully(GracefulShutdownCallback callback) {
        if (this.gracefulShutdown == null) {
            callback.shutdownComplete(GracefulShutdownResult.IMMEDIATE);
        } else {
            this.gracefulShutdown.shutDownGracefully(callback);
        }
    }

    public Server getServer() {
        return this.server;
    }
}
