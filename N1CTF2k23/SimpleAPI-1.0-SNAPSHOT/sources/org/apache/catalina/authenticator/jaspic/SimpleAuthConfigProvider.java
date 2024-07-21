package org.apache.catalina.authenticator.jaspic;

import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/authenticator/jaspic/SimpleAuthConfigProvider.class */
public class SimpleAuthConfigProvider implements AuthConfigProvider {
    private final Map<String, String> properties;
    private volatile ServerAuthConfig serverAuthConfig;

    public SimpleAuthConfigProvider(Map<String, String> properties, AuthConfigFactory factory) {
        this.properties = properties;
        if (factory != null) {
            factory.registerConfigProvider(this, null, null, "Automatic registration");
        }
    }

    @Override // javax.security.auth.message.config.AuthConfigProvider
    public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException {
        return null;
    }

    @Override // javax.security.auth.message.config.AuthConfigProvider
    public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException {
        ServerAuthConfig serverAuthConfig = this.serverAuthConfig;
        if (serverAuthConfig == null) {
            synchronized (this) {
                if (this.serverAuthConfig == null) {
                    this.serverAuthConfig = createServerAuthConfig(layer, appContext, handler, this.properties);
                }
                serverAuthConfig = this.serverAuthConfig;
            }
        }
        return serverAuthConfig;
    }

    protected ServerAuthConfig createServerAuthConfig(String layer, String appContext, CallbackHandler handler, Map<String, String> properties) {
        return new SimpleServerAuthConfig(layer, appContext, handler, properties);
    }

    @Override // javax.security.auth.message.config.AuthConfigProvider
    public void refresh() {
        ServerAuthConfig serverAuthConfig = this.serverAuthConfig;
        if (serverAuthConfig != null) {
            serverAuthConfig.refresh();
        }
    }
}
