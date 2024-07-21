package org.springframework.core.log;

import java.util.function.Function;
import org.apache.commons.logging.Log;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/log/LogFormatUtils.class */
public abstract class LogFormatUtils {
    public static String formatValue(@Nullable Object value, boolean limitLength) {
        String str;
        if (value == null) {
            return "";
        }
        if (value instanceof CharSequence) {
            str = "\"" + value + "\"";
        } else {
            try {
                str = value.toString();
            } catch (Throwable ex) {
                str = ex.toString();
            }
        }
        return (!limitLength || str.length() <= 100) ? str : str.substring(0, 100) + " (truncated)...";
    }

    public static void traceDebug(Log logger, Function<Boolean, String> messageFactory) {
        if (logger.isDebugEnabled()) {
            boolean traceEnabled = logger.isTraceEnabled();
            String logMessage = messageFactory.apply(Boolean.valueOf(traceEnabled));
            if (traceEnabled) {
                logger.trace(logMessage);
            } else {
                logger.debug(logMessage);
            }
        }
    }
}
