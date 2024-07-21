package org.springframework.scripting;

import java.io.IOException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scripting/ScriptSource.class */
public interface ScriptSource {
    String getScriptAsString() throws IOException;

    boolean isModified();

    @Nullable
    String suggestedClassName();
}
