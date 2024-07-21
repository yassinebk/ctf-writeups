package org.springframework.boot.web.servlet;

import javax.servlet.Filter;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/FilterRegistrationBean.class */
public class FilterRegistrationBean<T extends Filter> extends AbstractFilterRegistrationBean<T> {
    private T filter;

    public FilterRegistrationBean() {
        super(new ServletRegistrationBean[0]);
    }

    public FilterRegistrationBean(T filter, ServletRegistrationBean<?>... servletRegistrationBeans) {
        super(servletRegistrationBeans);
        Assert.notNull(filter, "Filter must not be null");
        this.filter = filter;
    }

    @Override // org.springframework.boot.web.servlet.AbstractFilterRegistrationBean
    public T getFilter() {
        return this.filter;
    }

    public void setFilter(T filter) {
        Assert.notNull(filter, "Filter must not be null");
        this.filter = filter;
    }
}
