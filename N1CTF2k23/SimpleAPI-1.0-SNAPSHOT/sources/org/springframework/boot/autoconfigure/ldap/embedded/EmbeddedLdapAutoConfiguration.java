package org.springframework.boot.autoconfigure.ldap.embedded;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.schema.Schema;
import com.unboundid.ldif.LDIFReader;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.util.StringUtils;
@EnableConfigurationProperties({LdapProperties.class, EmbeddedLdapProperties.class})
@AutoConfigureBefore({LdapAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({InMemoryDirectoryServer.class})
@Conditional({EmbeddedLdapCondition.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/embedded/EmbeddedLdapAutoConfiguration.class */
public class EmbeddedLdapAutoConfiguration {
    private static final String PROPERTY_SOURCE_NAME = "ldap.ports";
    private final EmbeddedLdapProperties embeddedProperties;
    private InMemoryDirectoryServer server;

    public EmbeddedLdapAutoConfiguration(EmbeddedLdapProperties embeddedProperties) {
        this.embeddedProperties = embeddedProperties;
    }

    @Bean
    public InMemoryDirectoryServer directoryServer(ApplicationContext applicationContext) throws LDAPException {
        String[] baseDn = StringUtils.toStringArray(this.embeddedProperties.getBaseDn());
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(baseDn);
        if (this.embeddedProperties.getCredential().isAvailable()) {
            config.addAdditionalBindCredentials(this.embeddedProperties.getCredential().getUsername(), this.embeddedProperties.getCredential().getPassword());
        }
        setSchema(config);
        InMemoryListenerConfig listenerConfig = InMemoryListenerConfig.createLDAPConfig("LDAP", this.embeddedProperties.getPort());
        config.setListenerConfigs(new InMemoryListenerConfig[]{listenerConfig});
        this.server = new InMemoryDirectoryServer(config);
        importLdif(applicationContext);
        this.server.startListening();
        setPortProperty(applicationContext, this.server.getListenPort());
        return this.server;
    }

    private void setSchema(InMemoryDirectoryServerConfig config) {
        if (!this.embeddedProperties.getValidation().isEnabled()) {
            config.setSchema((Schema) null);
            return;
        }
        Resource schema = this.embeddedProperties.getValidation().getSchema();
        if (schema != null) {
            setSchema(config, schema);
        }
    }

    private void setSchema(InMemoryDirectoryServerConfig config, Resource resource) {
        try {
            Schema defaultSchema = Schema.getDefaultStandardSchema();
            Schema schema = Schema.getSchema(resource.getInputStream());
            config.setSchema(Schema.mergeSchemas(new Schema[]{defaultSchema, schema}));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load schema " + resource.getDescription(), ex);
        }
    }

    private void importLdif(ApplicationContext applicationContext) throws LDAPException {
        String location = this.embeddedProperties.getLdif();
        if (StringUtils.hasText(location)) {
            try {
                Resource resource = applicationContext.getResource(location);
                if (resource.exists()) {
                    InputStream inputStream = resource.getInputStream();
                    this.server.importFromLDIF(true, new LDIFReader(inputStream));
                    if (inputStream != null) {
                        if (0 != 0) {
                            inputStream.close();
                        } else {
                            inputStream.close();
                        }
                    }
                }
            } catch (Exception ex) {
                throw new IllegalStateException("Unable to load LDIF " + location, ex);
            }
        }
    }

    private void setPortProperty(ApplicationContext context, int port) {
        if (context instanceof ConfigurableApplicationContext) {
            MutablePropertySources sources = ((ConfigurableApplicationContext) context).getEnvironment().getPropertySources();
            getLdapPorts(sources).put("local.ldap.port", Integer.valueOf(port));
        }
        if (context.getParent() != null) {
            setPortProperty(context.getParent(), port);
        }
    }

    private Map<String, Object> getLdapPorts(MutablePropertySources sources) {
        PropertySource<?> propertySource = sources.get(PROPERTY_SOURCE_NAME);
        if (propertySource == null) {
            propertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, new HashMap());
            sources.addFirst(propertySource);
        }
        return (Map) propertySource.getSource();
    }

    @PreDestroy
    public void close() {
        if (this.server != null) {
            this.server.shutDown(true);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/embedded/EmbeddedLdapAutoConfiguration$EmbeddedLdapCondition.class */
    static class EmbeddedLdapCondition extends SpringBootCondition {
        private static final Bindable<List<String>> STRING_LIST = Bindable.listOf(String.class);

        EmbeddedLdapCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("Embedded LDAP", new Object[0]);
            Environment environment = context.getEnvironment();
            if (environment != null && !((List) Binder.get(environment).bind("spring.ldap.embedded.base-dn", STRING_LIST).orElseGet(Collections::emptyList)).isEmpty()) {
                return ConditionOutcome.match(message.because("Found base-dn property"));
            }
            return ConditionOutcome.noMatch(message.because("No base-dn property found"));
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ContextSource.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/embedded/EmbeddedLdapAutoConfiguration$EmbeddedLdapContextConfiguration.class */
    static class EmbeddedLdapContextConfiguration {
        EmbeddedLdapContextConfiguration() {
        }

        @DependsOn({"directoryServer"})
        @ConditionalOnMissingBean
        @Bean
        LdapContextSource ldapContextSource(Environment environment, LdapProperties properties, EmbeddedLdapProperties embeddedProperties) {
            LdapContextSource source = new LdapContextSource();
            if (embeddedProperties.getCredential().isAvailable()) {
                source.setUserDn(embeddedProperties.getCredential().getUsername());
                source.setPassword(embeddedProperties.getCredential().getPassword());
            }
            source.setUrls(properties.determineUrls(environment));
            return source;
        }
    }
}
