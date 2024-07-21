package org.springframework.boot.autoconfigure.jms.activemq;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
@AutoConfigureBefore({JmsAutoConfiguration.class})
@EnableConfigurationProperties({ActiveMQProperties.class, JmsProperties.class})
@AutoConfigureAfter({JndiConnectionFactoryAutoConfiguration.class})
@ConditionalOnMissingBean({ConnectionFactory.class})
@Import({ActiveMQXAConnectionFactoryConfiguration.class, ActiveMQConnectionFactoryConfiguration.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ConnectionFactory.class, ActiveMQConnectionFactory.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQAutoConfiguration.class */
public class ActiveMQAutoConfiguration {
}
