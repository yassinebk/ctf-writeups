package org.springframework.boot.autoconfigure.ldap;

import java.util.Collections;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
@EnableConfigurationProperties({LdapProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ContextSource.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/LdapAutoConfiguration.class */
public class LdapAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public LdapContextSource ldapContextSource(LdapProperties properties, Environment environment, ObjectProvider<DirContextAuthenticationStrategy> dirContextAuthenticationStrategy) {
        LdapContextSource source = new LdapContextSource();
        source.getClass();
        dirContextAuthenticationStrategy.ifUnique(this::setAuthenticationStrategy);
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        PropertyMapper.Source from = propertyMapper.from((PropertyMapper) properties.getUsername());
        source.getClass();
        from.to(this::setUserDn);
        PropertyMapper.Source from2 = propertyMapper.from((PropertyMapper) properties.getPassword());
        source.getClass();
        from2.to(this::setPassword);
        PropertyMapper.Source from3 = propertyMapper.from((PropertyMapper) properties.getAnonymousReadOnly());
        source.getClass();
        from3.to((v1) -> {
            r1.setAnonymousReadOnly(v1);
        });
        PropertyMapper.Source from4 = propertyMapper.from((PropertyMapper) properties.getBase());
        source.getClass();
        from4.to(this::setBase);
        PropertyMapper.Source from5 = propertyMapper.from((PropertyMapper) properties.determineUrls(environment));
        source.getClass();
        from5.to(this::setUrls);
        propertyMapper.from((PropertyMapper) properties.getBaseEnvironment()).to(baseEnvironment -> {
            source.setBaseEnvironmentProperties(Collections.unmodifiableMap(baseEnvironment));
        });
        return source;
    }

    @ConditionalOnMissingBean({LdapOperations.class})
    @Bean
    public LdapTemplate ldapTemplate(ContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }
}
