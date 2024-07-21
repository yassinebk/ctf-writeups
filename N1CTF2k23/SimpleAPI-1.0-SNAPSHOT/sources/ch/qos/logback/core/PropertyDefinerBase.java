package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.PropertyDefiner;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/PropertyDefinerBase.class */
public abstract class PropertyDefinerBase extends ContextAwareBase implements PropertyDefiner {
    /* JADX INFO: Access modifiers changed from: protected */
    public static String booleanAsStr(boolean bool) {
        return bool ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
    }
}
