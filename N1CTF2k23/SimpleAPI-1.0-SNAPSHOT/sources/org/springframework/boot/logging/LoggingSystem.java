package org.springframework.boot.logging;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/LoggingSystem.class */
public abstract class LoggingSystem {
    public static final String SYSTEM_PROPERTY = LoggingSystem.class.getName();
    public static final String NONE = "none";
    public static final String ROOT_LOGGER_NAME = "ROOT";
    private static final Map<String, String> SYSTEMS;

    public abstract void beforeInitialize();

    static {
        Map<String, String> systems = new LinkedHashMap<>();
        systems.put("ch.qos.logback.core.Appender", "org.springframework.boot.logging.logback.LogbackLoggingSystem");
        systems.put("org.apache.logging.log4j.core.impl.Log4jContextFactory", "org.springframework.boot.logging.log4j2.Log4J2LoggingSystem");
        systems.put("java.util.logging.LogManager", "org.springframework.boot.logging.java.JavaLoggingSystem");
        SYSTEMS = Collections.unmodifiableMap(systems);
    }

    public void initialize(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile) {
    }

    public void cleanUp() {
    }

    public Runnable getShutdownHandler() {
        return null;
    }

    public Set<LogLevel> getSupportedLogLevels() {
        return EnumSet.allOf(LogLevel.class);
    }

    public void setLogLevel(String loggerName, LogLevel level) {
        throw new UnsupportedOperationException("Unable to set log level");
    }

    public List<LoggerConfiguration> getLoggerConfigurations() {
        throw new UnsupportedOperationException("Unable to get logger configurations");
    }

    public LoggerConfiguration getLoggerConfiguration(String loggerName) {
        throw new UnsupportedOperationException("Unable to get logger configuration");
    }

    public static LoggingSystem get(ClassLoader classLoader) {
        String loggingSystem = System.getProperty(SYSTEM_PROPERTY);
        if (StringUtils.hasLength(loggingSystem)) {
            if (NONE.equals(loggingSystem)) {
                return new NoOpLoggingSystem();
            }
            return get(classLoader, loggingSystem);
        }
        return (LoggingSystem) SYSTEMS.entrySet().stream().filter(entry -> {
            return ClassUtils.isPresent((String) entry.getKey(), classLoader);
        }).map(entry2 -> {
            return get(classLoader, (String) entry2.getValue());
        }).findFirst().orElseThrow(() -> {
            return new IllegalStateException("No suitable logging system located");
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LoggingSystem get(ClassLoader classLoader, String loggingSystemClass) {
        try {
            Class<?> systemClass = ClassUtils.forName(loggingSystemClass, classLoader);
            Constructor<?> constructor = systemClass.getDeclaredConstructor(ClassLoader.class);
            constructor.setAccessible(true);
            return (LoggingSystem) constructor.newInstance(classLoader);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/LoggingSystem$NoOpLoggingSystem.class */
    static class NoOpLoggingSystem extends LoggingSystem {
        NoOpLoggingSystem() {
        }

        @Override // org.springframework.boot.logging.LoggingSystem
        public void beforeInitialize() {
        }

        @Override // org.springframework.boot.logging.LoggingSystem
        public void setLogLevel(String loggerName, LogLevel level) {
        }

        @Override // org.springframework.boot.logging.LoggingSystem
        public List<LoggerConfiguration> getLoggerConfigurations() {
            return Collections.emptyList();
        }

        @Override // org.springframework.boot.logging.LoggingSystem
        public LoggerConfiguration getLoggerConfiguration(String loggerName) {
            return null;
        }
    }
}
