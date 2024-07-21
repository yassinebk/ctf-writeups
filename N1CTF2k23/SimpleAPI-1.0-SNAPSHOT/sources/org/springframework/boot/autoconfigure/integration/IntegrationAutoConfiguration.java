package org.springframework.boot.autoconfigure.integration;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import javax.management.MBeanServer;
import javax.sql.DataSource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketMessagingAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.config.IntegrationManagementConfigurer;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.integration.jdbc.store.JdbcMessageStore;
import org.springframework.integration.jmx.config.EnableIntegrationMBeanExport;
import org.springframework.integration.monitor.IntegrationMBeanExporter;
import org.springframework.integration.rsocket.ClientRSocketConnector;
import org.springframework.integration.rsocket.IntegrationRSocketEndpoint;
import org.springframework.integration.rsocket.ServerRSocketConnector;
import org.springframework.integration.rsocket.ServerRSocketMessageHandler;
import org.springframework.integration.rsocket.outbound.RSocketOutboundGateway;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.StringUtils;
@EnableConfigurationProperties({IntegrationProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({EnableIntegration.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, JmxAutoConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration.class */
public class IntegrationAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @EnableIntegration
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationConfiguration.class */
    protected static class IntegrationConfiguration {
        protected IntegrationConfiguration() {
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({EnableIntegrationMBeanExport.class})
    @ConditionalOnMissingBean(value = {IntegrationMBeanExporter.class}, search = SearchStrategy.CURRENT)
    @ConditionalOnBean({MBeanServer.class})
    @ConditionalOnProperty(prefix = "spring.jmx", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationJmxConfiguration.class */
    protected static class IntegrationJmxConfiguration {
        protected IntegrationJmxConfiguration() {
        }

        @Bean
        public IntegrationMBeanExporter integrationMbeanExporter(BeanFactory beanFactory, Environment environment) {
            IntegrationMBeanExporter exporter = new IntegrationMBeanExporter();
            String defaultDomain = environment.getProperty("spring.jmx.default-domain");
            if (StringUtils.hasLength(defaultDomain)) {
                exporter.setDefaultDomain(defaultDomain);
            }
            String serverBean = environment.getProperty("spring.jmx.server", "mbeanServer");
            exporter.setServer((MBeanServer) beanFactory.getBean(serverBean, MBeanServer.class));
            return exporter;
        }
    }

    @ConditionalOnMissingBean(value = {IntegrationManagementConfigurer.class}, name = {"integrationManagementConfigurer"}, search = SearchStrategy.CURRENT)
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({EnableIntegrationManagement.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationManagementConfiguration.class */
    protected static class IntegrationManagementConfiguration {
        protected IntegrationManagementConfiguration() {
        }

        @EnableIntegrationManagement(defaultCountsEnabled = "true")
        @Configuration(proxyBeanMethods = false)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationManagementConfiguration$EnableIntegrationManagementConfiguration.class */
        protected static class EnableIntegrationManagementConfiguration {
            protected EnableIntegrationManagementConfiguration() {
            }
        }
    }

    @ConditionalOnMissingBean({GatewayProxyFactoryBean.class})
    @Configuration(proxyBeanMethods = false)
    @Import({IntegrationAutoConfigurationScanRegistrar.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationComponentScanConfiguration.class */
    protected static class IntegrationComponentScanConfiguration {
        protected IntegrationComponentScanConfiguration() {
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({JdbcMessageStore.class})
    @ConditionalOnSingleCandidate(DataSource.class)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationJdbcConfiguration.class */
    protected static class IntegrationJdbcConfiguration {
        protected IntegrationJdbcConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        public IntegrationDataSourceInitializer integrationDataSourceInitializer(DataSource dataSource, ResourceLoader resourceLoader, IntegrationProperties properties) {
            return new IntegrationDataSourceInitializer(dataSource, resourceLoader, properties);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({IntegrationRSocketEndpoint.class, RSocketRequester.class, RSocketFactory.class})
    @Conditional({AnyRSocketChannelAdapterAvailable.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration.class */
    protected static class IntegrationRSocketConfiguration {
        protected IntegrationRSocketConfiguration() {
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$AnyRSocketChannelAdapterAvailable.class */
        static class AnyRSocketChannelAdapterAvailable extends AnyNestedCondition {
            AnyRSocketChannelAdapterAvailable() {
                super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
            }

            @ConditionalOnBean({IntegrationRSocketEndpoint.class})
            /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$AnyRSocketChannelAdapterAvailable$IntegrationRSocketEndpointAvailable.class */
            static class IntegrationRSocketEndpointAvailable {
                IntegrationRSocketEndpointAvailable() {
                }
            }

            @ConditionalOnBean({RSocketOutboundGateway.class})
            /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$AnyRSocketChannelAdapterAvailable$RSocketOutboundGatewayAvailable.class */
            static class RSocketOutboundGatewayAvailable {
                RSocketOutboundGatewayAvailable() {
                }
            }
        }

        @AutoConfigureBefore({RSocketMessagingAutoConfiguration.class})
        @Configuration(proxyBeanMethods = false)
        @ConditionalOnClass({TcpServerTransport.class})
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$IntegrationRSocketServerConfiguration.class */
        protected static class IntegrationRSocketServerConfiguration {
            protected IntegrationRSocketServerConfiguration() {
            }

            @ConditionalOnMissingBean({ServerRSocketMessageHandler.class})
            @Bean
            public RSocketMessageHandler serverRSocketMessageHandler(RSocketStrategies rSocketStrategies, IntegrationProperties integrationProperties) {
                ServerRSocketMessageHandler serverRSocketMessageHandler = new ServerRSocketMessageHandler(integrationProperties.getRsocket().getServer().isMessageMappingEnabled());
                serverRSocketMessageHandler.setRSocketStrategies(rSocketStrategies);
                return serverRSocketMessageHandler;
            }

            @ConditionalOnMissingBean
            @Bean
            public ServerRSocketConnector serverRSocketConnector(ServerRSocketMessageHandler messageHandler) {
                return new ServerRSocketConnector(messageHandler);
            }
        }

        @Configuration(proxyBeanMethods = false)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$IntegrationRSocketClientConfiguration.class */
        protected static class IntegrationRSocketClientConfiguration {
            protected IntegrationRSocketClientConfiguration() {
            }

            @ConditionalOnMissingBean
            @Conditional({RemoteRSocketServerAddressConfigured.class})
            @Bean
            public ClientRSocketConnector clientRSocketConnector(IntegrationProperties integrationProperties, RSocketStrategies rSocketStrategies) {
                ClientRSocketConnector clientRSocketConnector;
                IntegrationProperties.RSocket.Client client = integrationProperties.getRsocket().getClient();
                if (client.getUri() != null) {
                    clientRSocketConnector = new ClientRSocketConnector(client.getUri());
                } else {
                    clientRSocketConnector = new ClientRSocketConnector(client.getHost(), client.getPort().intValue());
                }
                ClientRSocketConnector clientRSocketConnector2 = clientRSocketConnector;
                clientRSocketConnector2.setRSocketStrategies(rSocketStrategies);
                return clientRSocketConnector2;
            }

            /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$IntegrationRSocketClientConfiguration$RemoteRSocketServerAddressConfigured.class */
            static class RemoteRSocketServerAddressConfigured extends AnyNestedCondition {
                RemoteRSocketServerAddressConfigured() {
                    super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
                }

                @ConditionalOnProperty(prefix = "spring.integration.rsocket.client", name = {"uri"})
                /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$IntegrationRSocketClientConfiguration$RemoteRSocketServerAddressConfigured$WebSocketAddressConfigured.class */
                static class WebSocketAddressConfigured {
                    WebSocketAddressConfigured() {
                    }
                }

                @ConditionalOnProperty(prefix = "spring.integration.rsocket.client", name = {"host", "port"})
                /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationRSocketConfiguration$IntegrationRSocketClientConfiguration$RemoteRSocketServerAddressConfigured$TcpAddressConfigured.class */
                static class TcpAddressConfigured {
                    TcpAddressConfigured() {
                    }
                }
            }
        }
    }
}
