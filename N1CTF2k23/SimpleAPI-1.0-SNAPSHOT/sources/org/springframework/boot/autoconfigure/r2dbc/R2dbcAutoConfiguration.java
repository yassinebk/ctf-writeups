package org.springframework.boot.autoconfigure.r2dbc;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({R2dbcProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ConnectionFactory.class})
@Import({ConnectionFactoryConfigurations.Pool.class, ConnectionFactoryConfigurations.Generic.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/R2dbcAutoConfiguration.class */
public class R2dbcAutoConfiguration {
}
