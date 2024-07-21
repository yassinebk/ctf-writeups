package org.springframework.boot.autoconfigure.web.embedded;

import io.undertow.UndertowOptions;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.xnio.Option;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/embedded/UndertowWebServerFactoryCustomizer.class */
public class UndertowWebServerFactoryCustomizer implements WebServerFactoryCustomizer<ConfigurableUndertowWebServerFactory>, Ordered {
    private final Environment environment;
    private final ServerProperties serverProperties;

    public UndertowWebServerFactoryCustomizer(Environment environment, ServerProperties serverProperties) {
        this.environment = environment;
        this.serverProperties = serverProperties;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return 0;
    }

    @Override // org.springframework.boot.web.server.WebServerFactoryCustomizer
    public void customize(ConfigurableUndertowWebServerFactory factory) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        FactoryOptions options = new FactoryOptions(factory);
        ServerProperties properties = this.serverProperties;
        properties.getClass();
        map.from(this::getMaxHttpHeaderSize).asInt((v0) -> {
            return v0.toBytes();
        }).when((v1) -> {
            return isPositive(v1);
        }).to(options.server(UndertowOptions.MAX_HEADER_SIZE));
        mapUndertowProperties(factory, options);
        mapAccessLogProperties(factory);
        PropertyMapper.Source from = map.from(this::getOrDeduceUseForwardHeaders);
        factory.getClass();
        from.to((v1) -> {
            r1.setUseForwardHeaders(v1);
        });
    }

    private void mapUndertowProperties(ConfigurableUndertowWebServerFactory factory, FactoryOptions options) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        ServerProperties.Undertow properties = this.serverProperties.getUndertow();
        properties.getClass();
        PropertyMapper.Source<Integer> asInt = map.from(this::getBufferSize).whenNonNull().asInt((v0) -> {
            return v0.toBytes();
        });
        factory.getClass();
        asInt.to(this::setBufferSize);
        ServerProperties.Undertow.Threads threadProperties = properties.getThreads();
        threadProperties.getClass();
        PropertyMapper.Source from = map.from(this::getIo);
        factory.getClass();
        from.to(this::setIoThreads);
        threadProperties.getClass();
        PropertyMapper.Source from2 = map.from(this::getWorker);
        factory.getClass();
        from2.to(this::setWorkerThreads);
        properties.getClass();
        PropertyMapper.Source from3 = map.from(this::getDirectBuffers);
        factory.getClass();
        from3.to(this::setUseDirectBuffers);
        properties.getClass();
        map.from(this::getMaxHttpPostSize).as((v0) -> {
            return v0.toBytes();
        }).when((v1) -> {
            return isPositive(v1);
        }).to(options.server(UndertowOptions.MAX_ENTITY_SIZE));
        properties.getClass();
        map.from(this::getMaxParameters).to(options.server(UndertowOptions.MAX_PARAMETERS));
        properties.getClass();
        map.from(this::getMaxHeaders).to(options.server(UndertowOptions.MAX_HEADERS));
        properties.getClass();
        map.from(this::getMaxCookies).to(options.server(UndertowOptions.MAX_COOKIES));
        properties.getClass();
        map.from(this::isAllowEncodedSlash).to(options.server(UndertowOptions.ALLOW_ENCODED_SLASH));
        properties.getClass();
        map.from(this::isDecodeUrl).to(options.server(UndertowOptions.DECODE_URL));
        properties.getClass();
        map.from(this::getUrlCharset).as((v0) -> {
            return v0.name();
        }).to(options.server(UndertowOptions.URL_CHARSET));
        properties.getClass();
        map.from(this::isAlwaysSetKeepAlive).to(options.server(UndertowOptions.ALWAYS_SET_KEEP_ALIVE));
        properties.getClass();
        map.from(this::getNoRequestTimeout).asInt((v0) -> {
            return v0.toMillis();
        }).to(options.server(UndertowOptions.NO_REQUEST_TIMEOUT));
        ServerProperties.Undertow.Options options2 = properties.getOptions();
        options2.getClass();
        PropertyMapper.Source from4 = map.from(this::getServer);
        options.getClass();
        from4.to(options.forEach(this::server));
        ServerProperties.Undertow.Options options3 = properties.getOptions();
        options3.getClass();
        PropertyMapper.Source from5 = map.from(this::getSocket);
        options.getClass();
        from5.to(options.forEach(this::socket));
    }

    private boolean isPositive(Number value) {
        return value.longValue() > 0;
    }

    private void mapAccessLogProperties(ConfigurableUndertowWebServerFactory factory) {
        ServerProperties.Undertow.Accesslog properties = this.serverProperties.getUndertow().getAccesslog();
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        properties.getClass();
        PropertyMapper.Source from = map.from(this::isEnabled);
        factory.getClass();
        from.to((v1) -> {
            r1.setAccessLogEnabled(v1);
        });
        properties.getClass();
        PropertyMapper.Source from2 = map.from(this::getDir);
        factory.getClass();
        from2.to(this::setAccessLogDirectory);
        properties.getClass();
        PropertyMapper.Source from3 = map.from(this::getPattern);
        factory.getClass();
        from3.to(this::setAccessLogPattern);
        properties.getClass();
        PropertyMapper.Source from4 = map.from(this::getPrefix);
        factory.getClass();
        from4.to(this::setAccessLogPrefix);
        properties.getClass();
        PropertyMapper.Source from5 = map.from(this::getSuffix);
        factory.getClass();
        from5.to(this::setAccessLogSuffix);
        properties.getClass();
        PropertyMapper.Source from6 = map.from(this::isRotate);
        factory.getClass();
        from6.to((v1) -> {
            r1.setAccessLogRotate(v1);
        });
    }

    private boolean getOrDeduceUseForwardHeaders() {
        if (this.serverProperties.getForwardHeadersStrategy() == null) {
            CloudPlatform platform = CloudPlatform.getActive(this.environment);
            return platform != null && platform.isUsingForwardHeaders();
        }
        return this.serverProperties.getForwardHeadersStrategy().equals(ServerProperties.ForwardHeadersStrategy.NATIVE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/embedded/UndertowWebServerFactoryCustomizer$FactoryOptions.class */
    public static class FactoryOptions {
        private static final Map<String, Option<?>> NAME_LOOKUP;
        private final ConfigurableUndertowWebServerFactory factory;

        static {
            Map<String, Option<?>> lookup = new HashMap<>();
            ReflectionUtils.doWithLocalFields(UndertowOptions.class, field -> {
                int modifiers = field.getModifiers();
                if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Option.class.isAssignableFrom(field.getType())) {
                    try {
                        Option<?> option = (Option) field.get(null);
                        lookup.put(getCanonicalName(field.getName()), option);
                    } catch (IllegalAccessException e) {
                    }
                }
            });
            NAME_LOOKUP = Collections.unmodifiableMap(lookup);
        }

        FactoryOptions(ConfigurableUndertowWebServerFactory factory) {
            this.factory = factory;
        }

        <T> Consumer<T> server(Option<T> option) {
            return value -> {
                this.factory.addBuilderCustomizers(builder -> {
                    builder.setServerOption(option, value);
                });
            };
        }

        <T> Consumer<T> socket(Option<T> option) {
            return value -> {
                this.factory.addBuilderCustomizers(builder -> {
                    builder.setSocketOption(option, value);
                });
            };
        }

        <T> Consumer<Map<String, String>> forEach(Function<Option<T>, Consumer<T>> function) {
            return map -> {
                map.forEach(key, value -> {
                    Option<?> option = NAME_LOOKUP.get(getCanonicalName(key));
                    Assert.state(option != null, "Unable to find '" + key + "' in UndertowOptions");
                    ((Consumer) function.apply(option)).accept(option.parseValue(value, getClass().getClassLoader()));
                });
            };
        }

        private static String getCanonicalName(String name) {
            StringBuilder canonicalName = new StringBuilder(name.length());
            name.chars().filter(Character::isLetterOrDigit).map(Character::toLowerCase).forEach(c -> {
                canonicalName.append((char) c);
            });
            return canonicalName.toString();
        }
    }
}
