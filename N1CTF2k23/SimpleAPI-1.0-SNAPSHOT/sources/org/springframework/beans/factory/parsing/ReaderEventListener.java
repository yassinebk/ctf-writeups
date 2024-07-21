package org.springframework.beans.factory.parsing;

import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/parsing/ReaderEventListener.class */
public interface ReaderEventListener extends EventListener {
    void defaultsRegistered(DefaultsDefinition defaultsDefinition);

    void componentRegistered(ComponentDefinition componentDefinition);

    void aliasRegistered(AliasDefinition aliasDefinition);

    void importProcessed(ImportDefinition importDefinition);
}
