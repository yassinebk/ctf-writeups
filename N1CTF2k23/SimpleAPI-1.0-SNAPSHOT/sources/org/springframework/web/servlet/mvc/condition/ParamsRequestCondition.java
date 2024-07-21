package org.springframework.web.servlet.mvc.condition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.WebUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/ParamsRequestCondition.class */
public final class ParamsRequestCondition extends AbstractRequestCondition<ParamsRequestCondition> {
    private final Set<ParamExpression> expressions;

    public ParamsRequestCondition(String... params) {
        this(parseExpressions(params));
    }

    private ParamsRequestCondition(Collection<ParamExpression> conditions) {
        this.expressions = Collections.unmodifiableSet(new LinkedHashSet(conditions));
    }

    private static Collection<ParamExpression> parseExpressions(String... params) {
        Set<ParamExpression> expressions = new LinkedHashSet<>();
        for (String param : params) {
            expressions.add(new ParamExpression(param));
        }
        return expressions;
    }

    public Set<NameValueExpression<String>> getExpressions() {
        return new LinkedHashSet(this.expressions);
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    protected Collection<ParamExpression> getContent() {
        return this.expressions;
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    protected String getToStringInfix() {
        return " && ";
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public ParamsRequestCondition combine(ParamsRequestCondition other) {
        Set<ParamExpression> set = new LinkedHashSet<>(this.expressions);
        set.addAll(other.expressions);
        return new ParamsRequestCondition(set);
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    @Nullable
    public ParamsRequestCondition getMatchingCondition(HttpServletRequest request) {
        for (ParamExpression expression : this.expressions) {
            if (!expression.match(request)) {
                return null;
            }
        }
        return this;
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public int compareTo(ParamsRequestCondition other, HttpServletRequest request) {
        int result = other.expressions.size() - this.expressions.size();
        if (result != 0) {
            return result;
        }
        return (int) (getValueMatchCount(other.expressions) - getValueMatchCount(this.expressions));
    }

    private long getValueMatchCount(Set<ParamExpression> expressions) {
        long count = 0;
        for (ParamExpression e : expressions) {
            if (e.getValue() != null && !e.isNegated()) {
                count++;
            }
        }
        return count;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/ParamsRequestCondition$ParamExpression.class */
    public static class ParamExpression extends AbstractNameValueExpression<String> {
        private final Set<String> namesToMatch;

        ParamExpression(String expression) {
            super(expression);
            String[] strArr;
            this.namesToMatch = new HashSet(WebUtils.SUBMIT_IMAGE_SUFFIXES.length + 1);
            this.namesToMatch.add(getName());
            for (String suffix : WebUtils.SUBMIT_IMAGE_SUFFIXES) {
                this.namesToMatch.add(getName() + suffix);
            }
        }

        @Override // org.springframework.web.servlet.mvc.condition.AbstractNameValueExpression
        protected boolean isCaseSensitiveName() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.web.servlet.mvc.condition.AbstractNameValueExpression
        public String parseValue(String valueExpression) {
            return valueExpression;
        }

        @Override // org.springframework.web.servlet.mvc.condition.AbstractNameValueExpression
        protected boolean matchName(HttpServletRequest request) {
            for (String current : this.namesToMatch) {
                if (request.getParameterMap().get(current) != null) {
                    return true;
                }
            }
            return request.getParameterMap().containsKey(this.name);
        }

        @Override // org.springframework.web.servlet.mvc.condition.AbstractNameValueExpression
        protected boolean matchValue(HttpServletRequest request) {
            return ObjectUtils.nullSafeEquals(this.value, request.getParameter(this.name));
        }
    }
}
