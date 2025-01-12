package org.springframework.boot.logging;

import org.springframework.boot.system.ApplicationPid;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/LoggingSystemProperties.class */
public class LoggingSystemProperties {
    public static final String PID_KEY = "PID";
    public static final String EXCEPTION_CONVERSION_WORD = "LOG_EXCEPTION_CONVERSION_WORD";
    public static final String LOG_FILE = "LOG_FILE";
    public static final String LOG_PATH = "LOG_PATH";
    public static final String CONSOLE_LOG_PATTERN = "CONSOLE_LOG_PATTERN";
    public static final String FILE_CLEAN_HISTORY_ON_START = "LOG_FILE_CLEAN_HISTORY_ON_START";
    public static final String FILE_LOG_PATTERN = "FILE_LOG_PATTERN";
    public static final String FILE_MAX_HISTORY = "LOG_FILE_MAX_HISTORY";
    public static final String FILE_MAX_SIZE = "LOG_FILE_MAX_SIZE";
    public static final String FILE_TOTAL_SIZE_CAP = "LOG_FILE_TOTAL_SIZE_CAP";
    public static final String LOG_LEVEL_PATTERN = "LOG_LEVEL_PATTERN";
    public static final String LOG_DATEFORMAT_PATTERN = "LOG_DATEFORMAT_PATTERN";
    public static final String ROLLING_FILE_NAME_PATTERN = "ROLLING_FILE_NAME_PATTERN";
    private final Environment environment;

    public LoggingSystemProperties(Environment environment) {
        Assert.notNull(environment, "Environment must not be null");
        this.environment = environment;
    }

    public void apply() {
        apply(null);
    }

    public void apply(LogFile logFile) {
        PropertyResolver resolver = getPropertyResolver();
        setSystemProperty(resolver, EXCEPTION_CONVERSION_WORD, "exception-conversion-word");
        setSystemProperty(PID_KEY, new ApplicationPid().toString());
        setSystemProperty(resolver, CONSOLE_LOG_PATTERN, "pattern.console");
        setSystemProperty(resolver, FILE_LOG_PATTERN, "pattern.file");
        setSystemProperty(resolver, FILE_CLEAN_HISTORY_ON_START, "file.clean-history-on-start");
        setSystemProperty(resolver, FILE_MAX_HISTORY, "file.max-history");
        setSystemProperty(resolver, FILE_MAX_SIZE, "file.max-size");
        setSystemProperty(resolver, FILE_TOTAL_SIZE_CAP, "file.total-size-cap");
        setSystemProperty(resolver, LOG_LEVEL_PATTERN, "pattern.level");
        setSystemProperty(resolver, LOG_DATEFORMAT_PATTERN, "pattern.dateformat");
        setSystemProperty(resolver, ROLLING_FILE_NAME_PATTERN, "pattern.rolling-file-name");
        if (logFile != null) {
            logFile.applyToSystemProperties();
        }
    }

    private PropertyResolver getPropertyResolver() {
        if (this.environment instanceof ConfigurableEnvironment) {
            PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(((ConfigurableEnvironment) this.environment).getPropertySources());
            resolver.setIgnoreUnresolvableNestedPlaceholders(true);
            return resolver;
        }
        return this.environment;
    }

    private void setSystemProperty(PropertyResolver resolver, String systemPropertyName, String propertyName) {
        setSystemProperty(systemPropertyName, resolver.getProperty("logging." + propertyName));
    }

    private void setSystemProperty(String name, String value) {
        if (System.getProperty(name) == null && value != null) {
            System.setProperty(name, value);
        }
    }
}
