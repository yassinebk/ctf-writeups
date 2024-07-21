package org.springframework.boot;

import java.util.List;
import java.util.Set;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/ApplicationArguments.class */
public interface ApplicationArguments {
    String[] getSourceArgs();

    Set<String> getOptionNames();

    boolean containsOption(String name);

    List<String> getOptionValues(String name);

    List<String> getNonOptionArgs();
}
