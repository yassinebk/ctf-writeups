package org.springframework.boot.logging;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/DeferredLog.class */
public class DeferredLog implements Log {
    private volatile Log destination;
    private final List<Line> lines = new ArrayList();

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        boolean z;
        synchronized (this.lines) {
            z = this.destination == null || this.destination.isTraceEnabled();
        }
        return z;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        boolean z;
        synchronized (this.lines) {
            z = this.destination == null || this.destination.isDebugEnabled();
        }
        return z;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        boolean z;
        synchronized (this.lines) {
            z = this.destination == null || this.destination.isInfoEnabled();
        }
        return z;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        boolean z;
        synchronized (this.lines) {
            z = this.destination == null || this.destination.isWarnEnabled();
        }
        return z;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        boolean z;
        synchronized (this.lines) {
            z = this.destination == null || this.destination.isErrorEnabled();
        }
        return z;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        boolean z;
        synchronized (this.lines) {
            z = this.destination == null || this.destination.isFatalEnabled();
        }
        return z;
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message) {
        log(LogLevel.TRACE, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message, Throwable t) {
        log(LogLevel.TRACE, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message) {
        log(LogLevel.DEBUG, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message, Throwable t) {
        log(LogLevel.DEBUG, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message) {
        log(LogLevel.INFO, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message, Throwable t) {
        log(LogLevel.INFO, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message) {
        log(LogLevel.WARN, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message, Throwable t) {
        log(LogLevel.WARN, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message) {
        log(LogLevel.ERROR, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message, Throwable t) {
        log(LogLevel.ERROR, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message) {
        log(LogLevel.FATAL, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message, Throwable t) {
        log(LogLevel.FATAL, message, t);
    }

    private void log(LogLevel level, Object message, Throwable t) {
        synchronized (this.lines) {
            if (this.destination != null) {
                logTo(this.destination, level, message, t);
            } else {
                this.lines.add(new Line(level, message, t));
            }
        }
    }

    public void switchTo(Class<?> destination) {
        switchTo(LogFactory.getLog(destination));
    }

    public void switchTo(Log destination) {
        synchronized (this.lines) {
            replayTo(destination);
            this.destination = destination;
        }
    }

    public void replayTo(Class<?> destination) {
        replayTo(LogFactory.getLog(destination));
    }

    public void replayTo(Log destination) {
        synchronized (this.lines) {
            for (Line line : this.lines) {
                logTo(destination, line.getLevel(), line.getMessage(), line.getThrowable());
            }
            this.lines.clear();
        }
    }

    public static Log replay(Log source, Class<?> destination) {
        return replay(source, LogFactory.getLog(destination));
    }

    public static Log replay(Log source, Log destination) {
        if (source instanceof DeferredLog) {
            ((DeferredLog) source).replayTo(destination);
        }
        return destination;
    }

    private static void logTo(Log log, LogLevel level, Object message, Throwable throwable) {
        switch (level) {
            case TRACE:
                log.trace(message, throwable);
                return;
            case DEBUG:
                log.debug(message, throwable);
                return;
            case INFO:
                log.info(message, throwable);
                return;
            case WARN:
                log.warn(message, throwable);
                return;
            case ERROR:
                log.error(message, throwable);
                return;
            case FATAL:
                log.fatal(message, throwable);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/DeferredLog$Line.class */
    public static class Line {
        private final LogLevel level;
        private final Object message;
        private final Throwable throwable;

        Line(LogLevel level, Object message, Throwable throwable) {
            this.level = level;
            this.message = message;
            this.throwable = throwable;
        }

        LogLevel getLevel() {
            return this.level;
        }

        Object getMessage() {
            return this.message;
        }

        Throwable getThrowable() {
            return this.throwable;
        }
    }
}
