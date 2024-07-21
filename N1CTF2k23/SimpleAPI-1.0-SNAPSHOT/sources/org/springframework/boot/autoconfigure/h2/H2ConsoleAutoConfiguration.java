package org.springframework.boot.autoconfigure.h2;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@EnableConfigurationProperties({H2ConsoleProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({WebServlet.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.h2.console", name = {"enabled"}, havingValue = "true", matchIfMissing = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/h2/H2ConsoleAutoConfiguration.class */
public class H2ConsoleAutoConfiguration {
    private static final Log logger = LogFactory.getLog(H2ConsoleAutoConfiguration.class);

    @Bean
    public ServletRegistrationBean<WebServlet> h2Console(H2ConsoleProperties properties, ObjectProvider<DataSource> dataSource) {
        String path = properties.getPath();
        String urlMapping = path + (path.endsWith("/") ? "*" : "/*");
        ServletRegistrationBean<WebServlet> registration = new ServletRegistrationBean<>(new WebServlet(), urlMapping);
        H2ConsoleProperties.Settings settings = properties.getSettings();
        if (settings.isTrace()) {
            registration.addInitParameter("trace", "");
        }
        if (settings.isWebAllowOthers()) {
            registration.addInitParameter("webAllowOthers", "");
        }
        dataSource.ifAvailable(available -> {
            try {
                Connection connection = available.getConnection();
                logger.info("H2 console available at '" + path + "'. Database available at '" + connection.getMetaData().getURL() + "'");
                if (connection != null) {
                    if (0 != 0) {
                        connection.close();
                    } else {
                        connection.close();
                    }
                }
            } catch (SQLException e) {
            }
        });
        return registration;
    }
}
