package org.springframework.web.servlet.handler;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/handler/BeanNameUrlHandlerMapping.class */
public class BeanNameUrlHandlerMapping extends AbstractDetectingUrlHandlerMapping {
    @Override // org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping
    protected String[] determineUrlsForHandler(String beanName) {
        List<String> urls = new ArrayList<>();
        if (beanName.startsWith("/")) {
            urls.add(beanName);
        }
        String[] aliases = obtainApplicationContext().getAliases(beanName);
        for (String alias : aliases) {
            if (alias.startsWith("/")) {
                urls.add(alias);
            }
        }
        return StringUtils.toStringArray(urls);
    }
}
