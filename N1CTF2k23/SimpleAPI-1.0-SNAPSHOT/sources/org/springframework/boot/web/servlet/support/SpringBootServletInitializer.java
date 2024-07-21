package org.springframework.boot.web.servlet.support;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.ParentContextApplicationContextInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/support/SpringBootServletInitializer.class */
public abstract class SpringBootServletInitializer implements WebApplicationInitializer {
    protected Log logger;
    private boolean registerErrorPageFilter = true;

    protected final void setRegisterErrorPageFilter(boolean registerErrorPageFilter) {
        this.registerErrorPageFilter = registerErrorPageFilter;
    }

    @Override // org.springframework.web.WebApplicationInitializer
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.logger = LogFactory.getLog(getClass());
        WebApplicationContext rootAppContext = createRootApplicationContext(servletContext);
        if (rootAppContext != null) {
            servletContext.addListener((ServletContext) new ContextLoaderListener(rootAppContext) { // from class: org.springframework.boot.web.servlet.support.SpringBootServletInitializer.1
                @Override // org.springframework.web.context.ContextLoaderListener, javax.servlet.ServletContextListener
                public void contextInitialized(ServletContextEvent event) {
                }

                @Override // org.springframework.web.context.ContextLoaderListener, javax.servlet.ServletContextListener
                public void contextDestroyed(ServletContextEvent event) {
                    try {
                        super.contextDestroyed(event);
                    } finally {
                        SpringBootServletInitializer.this.deregisterJdbcDrivers(event.getServletContext());
                    }
                }
            });
        } else {
            this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
        }
    }

    protected void deregisterJdbcDrivers(ServletContext servletContext) {
        Iterator it = Collections.list(DriverManager.getDrivers()).iterator();
        while (it.hasNext()) {
            Driver driver = (Driver) it.next();
            if (driver.getClass().getClassLoader() == servletContext.getClassLoader()) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                }
            }
        }
    }

    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder();
        builder.main(getClass());
        ApplicationContext parent = getExistingRootWebApplicationContext(servletContext);
        if (parent != null) {
            this.logger.info("Root context already created (using as parent).");
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, null);
            builder.initializers(new ParentContextApplicationContextInitializer(parent));
        }
        builder.initializers(new ServletContextApplicationContextInitializer(servletContext));
        builder.contextClass(AnnotationConfigServletWebServerApplicationContext.class);
        SpringApplicationBuilder builder2 = configure(builder);
        builder2.listeners(new WebEnvironmentPropertySourceInitializer(servletContext));
        SpringApplication application = builder2.build();
        if (application.getAllSources().isEmpty() && MergedAnnotations.from(getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).isPresent(Configuration.class)) {
            application.addPrimarySources(Collections.singleton(getClass()));
        }
        Assert.state(!application.getAllSources().isEmpty(), "No SpringApplication sources have been defined. Either override the configure method or add an @Configuration annotation");
        if (this.registerErrorPageFilter) {
            application.addPrimarySources(Collections.singleton(ErrorPageFilterConfiguration.class));
        }
        application.setRegisterShutdownHook(false);
        return run(application);
    }

    protected SpringApplicationBuilder createSpringApplicationBuilder() {
        return new SpringApplicationBuilder(new Class[0]);
    }

    protected WebApplicationContext run(SpringApplication application) {
        return (WebApplicationContext) application.run(new String[0]);
    }

    private ApplicationContext getExistingRootWebApplicationContext(ServletContext servletContext) {
        Object context = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (context instanceof ApplicationContext) {
            return (ApplicationContext) context;
        }
        return null;
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/support/SpringBootServletInitializer$WebEnvironmentPropertySourceInitializer.class */
    public static final class WebEnvironmentPropertySourceInitializer implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
        private final ServletContext servletContext;

        private WebEnvironmentPropertySourceInitializer(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override // org.springframework.context.ApplicationListener
        public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
            ConfigurableEnvironment environment = event.getEnvironment();
            if (environment instanceof ConfigurableWebEnvironment) {
                ((ConfigurableWebEnvironment) environment).initPropertySources(this.servletContext, null);
            }
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return Integer.MIN_VALUE;
        }
    }
}
