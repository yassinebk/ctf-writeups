package org.springframework.beans.factory.parsing;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/parsing/ProblemReporter.class */
public interface ProblemReporter {
    void fatal(Problem problem);

    void error(Problem problem);

    void warning(Problem problem);
}
