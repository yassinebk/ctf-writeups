package org.springframework.boot.web.servlet.filter;

import org.springframework.web.filter.FormContentFilter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/filter/OrderedFormContentFilter.class */
public class OrderedFormContentFilter extends FormContentFilter implements OrderedFilter {
    public static final int DEFAULT_ORDER = -9900;
    private int order = DEFAULT_ORDER;

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
