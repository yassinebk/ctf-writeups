package org.springframework.beans.factory.parsing;

import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/parsing/QualifierEntry.class */
public class QualifierEntry implements ParseState.Entry {
    private String typeName;

    public QualifierEntry(String typeName) {
        if (!StringUtils.hasText(typeName)) {
            throw new IllegalArgumentException("Invalid qualifier type '" + typeName + "'.");
        }
        this.typeName = typeName;
    }

    public String toString() {
        return "Qualifier '" + this.typeName + "'";
    }
}
