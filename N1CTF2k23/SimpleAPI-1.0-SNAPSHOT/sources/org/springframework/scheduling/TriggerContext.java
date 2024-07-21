package org.springframework.scheduling;

import java.util.Date;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/TriggerContext.class */
public interface TriggerContext {
    @Nullable
    Date lastScheduledExecutionTime();

    @Nullable
    Date lastActualExecutionTime();

    @Nullable
    Date lastCompletionTime();
}
