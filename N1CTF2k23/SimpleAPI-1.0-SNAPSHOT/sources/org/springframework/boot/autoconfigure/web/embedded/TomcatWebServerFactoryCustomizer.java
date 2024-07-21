package org.springframework.boot.autoconfigure.web.embedded;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.valves.RemoteIpValve;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/embedded/TomcatWebServerFactoryCustomizer.class */
public class TomcatWebServerFactoryCustomizer implements WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory>, Ordered {
    private final Environment environment;
    private final ServerProperties serverProperties;

    public TomcatWebServerFactoryCustomizer(Environment environment, ServerProperties serverProperties) {
        this.environment = environment;
        this.serverProperties = serverProperties;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return 0;
    }

    @Override // org.springframework.boot.web.server.WebServerFactoryCustomizer
    public void customize(ConfigurableTomcatWebServerFactory factory) {
        ServerProperties properties = this.serverProperties;
        ServerProperties.Tomcat tomcatProperties = properties.getTomcat();
        PropertyMapper propertyMapper = PropertyMapper.get();
        tomcatProperties.getClass();
        PropertyMapper.Source whenNonNull = propertyMapper.from(this::getBasedir).whenNonNull();
        factory.getClass();
        whenNonNull.to(this::setBaseDirectory);
        tomcatProperties.getClass();
        PropertyMapper.Source as = propertyMapper.from(this::getBackgroundProcessorDelay).whenNonNull().as((v0) -> {
            return v0.getSeconds();
        }).as((v0) -> {
            return v0.intValue();
        });
        factory.getClass();
        as.to((v1) -> {
            r1.setBackgroundProcessorDelay(v1);
        });
        customizeRemoteIpValve(factory);
        ServerProperties.Tomcat.Threads threadProperties = tomcatProperties.getThreads();
        threadProperties.getClass();
        propertyMapper.from(this::getMax).when((v1) -> {
            return isPositive(v1);
        }).to(maxThreads -> {
            customizeMaxThreads(factory, threadProperties.getMax());
        });
        threadProperties.getClass();
        propertyMapper.from(this::getMinSpare).when((v1) -> {
            return isPositive(v1);
        }).to(minSpareThreads -> {
            customizeMinThreads(factory, minSpareThreads.intValue());
        });
        propertyMapper.from((PropertyMapper) this.serverProperties.getMaxHttpHeaderSize()).whenNonNull().asInt((v0) -> {
            return v0.toBytes();
        }).when((v1) -> {
            return isPositive(v1);
        }).to(maxHttpHeaderSize -> {
            customizeMaxHttpHeaderSize(factory, maxHttpHeaderSize.intValue());
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getMaxSwallowSize).whenNonNull().asInt((v0) -> {
            return v0.toBytes();
        }).to(maxSwallowSize -> {
            customizeMaxSwallowSize(factory, maxSwallowSize.intValue());
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getMaxHttpFormPostSize).asInt((v0) -> {
            return v0.toBytes();
        }).when(maxHttpFormPostSize -> {
            return maxHttpFormPostSize.intValue() != 0;
        }).to(maxHttpFormPostSize2 -> {
            customizeMaxHttpFormPostSize(factory, maxHttpFormPostSize2.intValue());
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getAccesslog).when((v0) -> {
            return v0.isEnabled();
        }).to(enabled -> {
            customizeAccessLog(factory);
        });
        tomcatProperties.getClass();
        PropertyMapper.Source whenNonNull2 = propertyMapper.from(this::getUriEncoding).whenNonNull();
        factory.getClass();
        whenNonNull2.to(this::setUriEncoding);
        tomcatProperties.getClass();
        propertyMapper.from(this::getConnectionTimeout).whenNonNull().to(connectionTimeout -> {
            customizeConnectionTimeout(factory, connectionTimeout);
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getMaxConnections).when((v1) -> {
            return isPositive(v1);
        }).to(maxConnections -> {
            customizeMaxConnections(factory, maxConnections.intValue());
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getAcceptCount).when((v1) -> {
            return isPositive(v1);
        }).to(acceptCount -> {
            customizeAcceptCount(factory, acceptCount.intValue());
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getProcessorCache).to(processorCache -> {
            customizeProcessorCache(factory, processorCache.intValue());
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getRelaxedPathChars).as(this::joinCharacters).whenHasText().to(relaxedChars -> {
            customizeRelaxedPathChars(factory, relaxedChars);
        });
        tomcatProperties.getClass();
        propertyMapper.from(this::getRelaxedQueryChars).as(this::joinCharacters).whenHasText().to(relaxedChars2 -> {
            customizeRelaxedQueryChars(factory, relaxedChars2);
        });
        customizeStaticResources(factory);
        customizeErrorReportValve(properties.getError(), factory);
    }

    private boolean isPositive(int value) {
        return value > 0;
    }

    private void customizeAcceptCount(ConfigurableTomcatWebServerFactory factory, int acceptCount) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol<?> protocol = (AbstractProtocol) handler;
                protocol.setAcceptCount(acceptCount);
            }
        });
    }

    private void customizeProcessorCache(ConfigurableTomcatWebServerFactory factory, int processorCache) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol) {
                ((AbstractProtocol) handler).setProcessorCache(processorCache);
            }
        });
    }

    private void customizeMaxConnections(ConfigurableTomcatWebServerFactory factory, int maxConnections) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol<?> protocol = (AbstractProtocol) handler;
                protocol.setMaxConnections(maxConnections);
            }
        });
    }

    private void customizeConnectionTimeout(ConfigurableTomcatWebServerFactory factory, Duration connectionTimeout) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol<?> protocol = (AbstractProtocol) handler;
                protocol.setConnectionTimeout((int) connectionTimeout.toMillis());
            }
        });
    }

    private void customizeRelaxedPathChars(ConfigurableTomcatWebServerFactory factory, String relaxedChars) {
        factory.addConnectorCustomizers(connector -> {
            connector.setAttribute("relaxedPathChars", relaxedChars);
        });
    }

    private void customizeRelaxedQueryChars(ConfigurableTomcatWebServerFactory factory, String relaxedChars) {
        factory.addConnectorCustomizers(connector -> {
            connector.setAttribute("relaxedQueryChars", relaxedChars);
        });
    }

    private String joinCharacters(List<Character> content) {
        return (String) content.stream().map((v0) -> {
            return String.valueOf(v0);
        }).collect(Collectors.joining());
    }

    private void customizeRemoteIpValve(ConfigurableTomcatWebServerFactory factory) {
        ServerProperties.Tomcat.Remoteip remoteIpProperties = this.serverProperties.getTomcat().getRemoteip();
        String protocolHeader = remoteIpProperties.getProtocolHeader();
        String remoteIpHeader = remoteIpProperties.getRemoteIpHeader();
        if (StringUtils.hasText(protocolHeader) || StringUtils.hasText(remoteIpHeader) || getOrDeduceUseForwardHeaders()) {
            RemoteIpValve valve = new RemoteIpValve();
            valve.setProtocolHeader(StringUtils.hasLength(protocolHeader) ? protocolHeader : "X-Forwarded-Proto");
            if (StringUtils.hasLength(remoteIpHeader)) {
                valve.setRemoteIpHeader(remoteIpHeader);
            }
            valve.setInternalProxies(remoteIpProperties.getInternalProxies());
            try {
                valve.setHostHeader(remoteIpProperties.getHostHeader());
            } catch (NoSuchMethodError e) {
            }
            valve.setPortHeader(remoteIpProperties.getPortHeader());
            valve.setProtocolHeaderHttpsValue(remoteIpProperties.getProtocolHeaderHttpsValue());
            factory.addEngineValves(valve);
        }
    }

    private boolean getOrDeduceUseForwardHeaders() {
        if (this.serverProperties.getForwardHeadersStrategy() == null) {
            CloudPlatform platform = CloudPlatform.getActive(this.environment);
            return platform != null && platform.isUsingForwardHeaders();
        }
        return this.serverProperties.getForwardHeadersStrategy().equals(ServerProperties.ForwardHeadersStrategy.NATIVE);
    }

    private void customizeMaxThreads(ConfigurableTomcatWebServerFactory factory, int maxThreads) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol protocol = (AbstractProtocol) handler;
                protocol.setMaxThreads(maxThreads);
            }
        });
    }

    private void customizeMinThreads(ConfigurableTomcatWebServerFactory factory, int minSpareThreads) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol protocol = (AbstractProtocol) handler;
                protocol.setMinSpareThreads(minSpareThreads);
            }
        });
    }

    private void customizeMaxHttpHeaderSize(ConfigurableTomcatWebServerFactory factory, int maxHttpHeaderSize) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractHttp11Protocol) {
                AbstractHttp11Protocol protocol = (AbstractHttp11Protocol) handler;
                protocol.setMaxHttpHeaderSize(maxHttpHeaderSize);
            }
        });
    }

    private void customizeMaxSwallowSize(ConfigurableTomcatWebServerFactory factory, int maxSwallowSize) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractHttp11Protocol) {
                AbstractHttp11Protocol<?> protocol = (AbstractHttp11Protocol) handler;
                protocol.setMaxSwallowSize(maxSwallowSize);
            }
        });
    }

    private void customizeMaxHttpFormPostSize(ConfigurableTomcatWebServerFactory factory, int maxHttpFormPostSize) {
        factory.addConnectorCustomizers(connector -> {
            connector.setMaxPostSize(maxHttpFormPostSize);
        });
    }

    private void customizeAccessLog(ConfigurableTomcatWebServerFactory factory) {
        ServerProperties.Tomcat tomcatProperties = this.serverProperties.getTomcat();
        AccessLogValve valve = new AccessLogValve();
        PropertyMapper map = PropertyMapper.get();
        ServerProperties.Tomcat.Accesslog accessLogConfig = tomcatProperties.getAccesslog();
        PropertyMapper.Source from = map.from((PropertyMapper) accessLogConfig.getConditionIf());
        valve.getClass();
        from.to(this::setConditionIf);
        PropertyMapper.Source from2 = map.from((PropertyMapper) accessLogConfig.getConditionUnless());
        valve.getClass();
        from2.to(this::setConditionUnless);
        PropertyMapper.Source from3 = map.from((PropertyMapper) accessLogConfig.getPattern());
        valve.getClass();
        from3.to(this::setPattern);
        PropertyMapper.Source from4 = map.from((PropertyMapper) accessLogConfig.getDirectory());
        valve.getClass();
        from4.to(this::setDirectory);
        PropertyMapper.Source from5 = map.from((PropertyMapper) accessLogConfig.getPrefix());
        valve.getClass();
        from5.to(this::setPrefix);
        PropertyMapper.Source from6 = map.from((PropertyMapper) accessLogConfig.getSuffix());
        valve.getClass();
        from6.to(this::setSuffix);
        PropertyMapper.Source whenHasText = map.from((PropertyMapper) accessLogConfig.getEncoding()).whenHasText();
        valve.getClass();
        whenHasText.to(this::setEncoding);
        PropertyMapper.Source whenHasText2 = map.from((PropertyMapper) accessLogConfig.getLocale()).whenHasText();
        valve.getClass();
        whenHasText2.to(this::setLocale);
        PropertyMapper.Source from7 = map.from((PropertyMapper) Boolean.valueOf(accessLogConfig.isCheckExists()));
        valve.getClass();
        from7.to((v1) -> {
            r1.setCheckExists(v1);
        });
        PropertyMapper.Source from8 = map.from((PropertyMapper) Boolean.valueOf(accessLogConfig.isRotate()));
        valve.getClass();
        from8.to((v1) -> {
            r1.setRotatable(v1);
        });
        PropertyMapper.Source from9 = map.from((PropertyMapper) Boolean.valueOf(accessLogConfig.isRenameOnRotate()));
        valve.getClass();
        from9.to((v1) -> {
            r1.setRenameOnRotate(v1);
        });
        PropertyMapper.Source from10 = map.from((PropertyMapper) Integer.valueOf(accessLogConfig.getMaxDays()));
        valve.getClass();
        from10.to((v1) -> {
            r1.setMaxDays(v1);
        });
        PropertyMapper.Source from11 = map.from((PropertyMapper) accessLogConfig.getFileDateFormat());
        valve.getClass();
        from11.to(this::setFileDateFormat);
        PropertyMapper.Source from12 = map.from((PropertyMapper) Boolean.valueOf(accessLogConfig.isIpv6Canonical()));
        valve.getClass();
        from12.to((v1) -> {
            r1.setIpv6Canonical(v1);
        });
        PropertyMapper.Source from13 = map.from((PropertyMapper) Boolean.valueOf(accessLogConfig.isRequestAttributesEnabled()));
        valve.getClass();
        from13.to((v1) -> {
            r1.setRequestAttributesEnabled(v1);
        });
        PropertyMapper.Source from14 = map.from((PropertyMapper) Boolean.valueOf(accessLogConfig.isBuffered()));
        valve.getClass();
        from14.to((v1) -> {
            r1.setBuffered(v1);
        });
        factory.addEngineValves(valve);
    }

    private void customizeStaticResources(ConfigurableTomcatWebServerFactory factory) {
        ServerProperties.Tomcat.Resource resource = this.serverProperties.getTomcat().getResource();
        factory.addContextCustomizers(context -> {
            context.addLifecycleListener(event -> {
                if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                    context.getResources().setCachingAllowed(resource.isAllowCaching());
                    if (resource.getCacheTtl() != null) {
                        long ttl = resource.getCacheTtl().toMillis();
                        context.getResources().setCacheTtl(ttl);
                    }
                }
            });
        });
    }

    private void customizeErrorReportValve(ErrorProperties error, ConfigurableTomcatWebServerFactory factory) {
        if (error.getIncludeStacktrace() == ErrorProperties.IncludeStacktrace.NEVER) {
            factory.addContextCustomizers(context -> {
                ErrorReportValve valve = new ErrorReportValve();
                valve.setShowServerInfo(false);
                valve.setShowReport(false);
                context.getParent().getPipeline().addValve(valve);
            });
        }
    }
}
