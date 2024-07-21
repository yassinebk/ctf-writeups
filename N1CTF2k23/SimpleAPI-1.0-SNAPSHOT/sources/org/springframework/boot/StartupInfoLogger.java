package org.springframework.boot;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.context.ApplicationContext;
import org.springframework.core.log.LogMessage;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/StartupInfoLogger.class */
public class StartupInfoLogger {
    private static final Log logger = LogFactory.getLog(StartupInfoLogger.class);
    private static final long HOST_NAME_RESOLVE_THRESHOLD = 200;
    private final Class<?> sourceClass;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StartupInfoLogger(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logStarting(Log applicationLog) {
        Assert.notNull(applicationLog, "Log must not be null");
        applicationLog.info(LogMessage.of(this::getStartingMessage));
        applicationLog.debug(LogMessage.of(this::getRunningMessage));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logStarted(Log applicationLog, StopWatch stopWatch) {
        if (applicationLog.isInfoEnabled()) {
            applicationLog.info(getStartedMessage(stopWatch));
        }
    }

    private CharSequence getStartingMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Starting ");
        appendApplicationName(message);
        appendVersion(message, this.sourceClass);
        appendOn(message);
        appendPid(message);
        appendContext(message);
        return message;
    }

    private CharSequence getRunningMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Running with Spring Boot");
        appendVersion(message, getClass());
        message.append(", Spring");
        appendVersion(message, ApplicationContext.class);
        return message;
    }

    private CharSequence getStartedMessage(StopWatch stopWatch) {
        StringBuilder message = new StringBuilder();
        message.append("Started ");
        appendApplicationName(message);
        message.append(" in ");
        message.append(stopWatch.getTotalTimeMillis() / 1000.0d);
        message.append(" seconds");
        try {
            double uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0d;
            message.append(" (JVM running for ").append(uptime).append(")");
        } catch (Throwable th) {
        }
        return message;
    }

    private void appendApplicationName(StringBuilder message) {
        String name = this.sourceClass != null ? ClassUtils.getShortName(this.sourceClass) : "application";
        message.append(name);
    }

    private void appendVersion(StringBuilder message, Class<?> source) {
        append(message, "v", () -> {
            return source.getPackage().getImplementationVersion();
        });
    }

    private void appendOn(StringBuilder message) {
        long startTime = System.currentTimeMillis();
        append(message, "on ", () -> {
            return InetAddress.getLocalHost().getHostName();
        });
        long resolveTime = System.currentTimeMillis() - startTime;
        if (resolveTime > HOST_NAME_RESOLVE_THRESHOLD) {
            logger.warn(LogMessage.of(() -> {
                StringBuilder warning = new StringBuilder();
                warning.append("InetAddress.getLocalHost().getHostName() took ");
                warning.append(resolveTime);
                warning.append(" milliseconds to respond.");
                warning.append(" Please verify your network configuration");
                if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    warning.append(" (macOS machines may need to add entries to /etc/hosts)");
                }
                warning.append(".");
                return warning;
            }));
        }
    }

    private void appendPid(StringBuilder message) {
        append(message, "with PID ", ApplicationPid::new);
    }

    private void appendContext(StringBuilder message) {
        StringBuilder context = new StringBuilder();
        ApplicationHome home = new ApplicationHome(this.sourceClass);
        if (home.getSource() != null) {
            context.append(home.getSource().getAbsolutePath());
        }
        append(context, "started by ", () -> {
            return System.getProperty("user.name");
        });
        append(context, "in ", () -> {
            return System.getProperty("user.dir");
        });
        if (context.length() > 0) {
            message.append(" (");
            message.append((CharSequence) context);
            message.append(")");
        }
    }

    private void append(StringBuilder message, String prefix, Callable<Object> call) {
        append(message, prefix, call, "");
    }

    private void append(StringBuilder message, String prefix, Callable<Object> call, String defaultValue) {
        Object result = callIfPossible(call);
        String value = result != null ? result.toString() : null;
        if (!StringUtils.hasLength(value)) {
            value = defaultValue;
        }
        if (StringUtils.hasLength(value)) {
            message.append(message.length() > 0 ? " " : "");
            message.append(prefix);
            message.append(value);
        }
    }

    private Object callIfPossible(Callable<Object> call) {
        try {
            return call.call();
        } catch (Exception e) {
            return null;
        }
    }
}
