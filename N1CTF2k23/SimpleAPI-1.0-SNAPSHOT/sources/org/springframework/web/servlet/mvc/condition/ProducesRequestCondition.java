package org.springframework.web.servlet.mvc.condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/ProducesRequestCondition.class */
public final class ProducesRequestCondition extends AbstractRequestCondition<ProducesRequestCondition> {
    private static final ContentNegotiationManager DEFAULT_CONTENT_NEGOTIATION_MANAGER = new ContentNegotiationManager();
    private static final ProducesRequestCondition EMPTY_CONDITION = new ProducesRequestCondition(new String[0]);
    private static final List<ProduceMediaTypeExpression> MEDIA_TYPE_ALL_LIST = Collections.singletonList(new ProduceMediaTypeExpression("*/*"));
    private static final String MEDIA_TYPES_ATTRIBUTE = ProducesRequestCondition.class.getName() + ".MEDIA_TYPES";
    private final List<ProduceMediaTypeExpression> expressions;
    private final ContentNegotiationManager contentNegotiationManager;

    public ProducesRequestCondition(String... produces) {
        this(produces, null, null);
    }

    public ProducesRequestCondition(String[] produces, @Nullable String[] headers) {
        this(produces, headers, null);
    }

    public ProducesRequestCondition(String[] produces, @Nullable String[] headers, @Nullable ContentNegotiationManager manager) {
        this.expressions = new ArrayList(parseExpressions(produces, headers));
        if (this.expressions.size() > 1) {
            Collections.sort(this.expressions);
        }
        this.contentNegotiationManager = manager != null ? manager : DEFAULT_CONTENT_NEGOTIATION_MANAGER;
    }

    private ProducesRequestCondition(List<ProduceMediaTypeExpression> expressions, ProducesRequestCondition other) {
        this.expressions = expressions;
        this.contentNegotiationManager = other.contentNegotiationManager;
    }

    private Set<ProduceMediaTypeExpression> parseExpressions(String[] produces, @Nullable String[] headers) {
        Set<ProduceMediaTypeExpression> result = new LinkedHashSet<>();
        if (headers != null) {
            for (String header : headers) {
                HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
                if (HttpHeaders.ACCEPT.equalsIgnoreCase(expr.name) && expr.value != 0) {
                    for (MediaType mediaType : MediaType.parseMediaTypes((String) expr.value)) {
                        result.add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
                    }
                }
            }
        }
        for (String produce : produces) {
            result.add(new ProduceMediaTypeExpression(produce));
        }
        return result;
    }

    public Set<MediaTypeExpression> getExpressions() {
        return new LinkedHashSet(this.expressions);
    }

    public Set<MediaType> getProducibleMediaTypes() {
        Set<MediaType> result = new LinkedHashSet<>();
        for (ProduceMediaTypeExpression expression : this.expressions) {
            if (!expression.isNegated()) {
                result.add(expression.getMediaType());
            }
        }
        return result;
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    public boolean isEmpty() {
        return this.expressions.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    public List<ProduceMediaTypeExpression> getContent() {
        return this.expressions;
    }

    @Override // org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
    protected String getToStringInfix() {
        return " || ";
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public ProducesRequestCondition combine(ProducesRequestCondition other) {
        return !other.expressions.isEmpty() ? other : this;
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    @Nullable
    public ProducesRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return EMPTY_CONDITION;
        }
        if (isEmpty()) {
            return this;
        }
        try {
            List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
            List<ProduceMediaTypeExpression> result = getMatchingExpressions(acceptedMediaTypes);
            if (!CollectionUtils.isEmpty(result)) {
                return new ProducesRequestCondition(result, this);
            }
            if (MediaType.ALL.isPresentIn(acceptedMediaTypes)) {
                return EMPTY_CONDITION;
            }
            return null;
        } catch (HttpMediaTypeException e) {
            return null;
        }
    }

    @Nullable
    private List<ProduceMediaTypeExpression> getMatchingExpressions(List<MediaType> acceptedMediaTypes) {
        List<ProduceMediaTypeExpression> result = null;
        for (ProduceMediaTypeExpression expression : this.expressions) {
            if (expression.match(acceptedMediaTypes)) {
                result = result != null ? result : new ArrayList<>();
                result.add(expression);
            }
        }
        return result;
    }

    @Override // org.springframework.web.servlet.mvc.condition.RequestCondition
    public int compareTo(ProducesRequestCondition other, HttpServletRequest request) {
        try {
            List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                int thisIndex = indexOfEqualMediaType(acceptedMediaType);
                int otherIndex = other.indexOfEqualMediaType(acceptedMediaType);
                int result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
                if (result != 0) {
                    return result;
                }
                int thisIndex2 = indexOfIncludedMediaType(acceptedMediaType);
                int otherIndex2 = other.indexOfIncludedMediaType(acceptedMediaType);
                int result2 = compareMatchingMediaTypes(this, thisIndex2, other, otherIndex2);
                if (result2 != 0) {
                    return result2;
                }
            }
            return 0;
        } catch (HttpMediaTypeNotAcceptableException ex) {
            throw new IllegalStateException("Cannot compare without having any requested media types", ex);
        }
    }

    private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {
        List<MediaType> result = (List) request.getAttribute(MEDIA_TYPES_ATTRIBUTE);
        if (result == null) {
            result = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
            request.setAttribute(MEDIA_TYPES_ATTRIBUTE, result);
        }
        return result;
    }

    private int indexOfEqualMediaType(MediaType mediaType) {
        for (int i = 0; i < getExpressionsToCompare().size(); i++) {
            MediaType currentMediaType = getExpressionsToCompare().get(i).getMediaType();
            if (mediaType.getType().equalsIgnoreCase(currentMediaType.getType()) && mediaType.getSubtype().equalsIgnoreCase(currentMediaType.getSubtype())) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfIncludedMediaType(MediaType mediaType) {
        for (int i = 0; i < getExpressionsToCompare().size(); i++) {
            if (mediaType.includes(getExpressionsToCompare().get(i).getMediaType())) {
                return i;
            }
        }
        return -1;
    }

    private int compareMatchingMediaTypes(ProducesRequestCondition condition1, int index1, ProducesRequestCondition condition2, int index2) {
        int result = 0;
        if (index1 != index2) {
            result = index2 - index1;
        } else if (index1 != -1) {
            ProduceMediaTypeExpression expr1 = condition1.getExpressionsToCompare().get(index1);
            ProduceMediaTypeExpression expr2 = condition2.getExpressionsToCompare().get(index2);
            int result2 = expr1.compareTo((AbstractMediaTypeExpression) expr2);
            result = result2 != 0 ? result2 : expr1.getMediaType().compareTo((MimeType) expr2.getMediaType());
        }
        return result;
    }

    private List<ProduceMediaTypeExpression> getExpressionsToCompare() {
        return this.expressions.isEmpty() ? MEDIA_TYPE_ALL_LIST : this.expressions;
    }

    public static void clearMediaTypesAttribute(HttpServletRequest request) {
        request.removeAttribute(MEDIA_TYPES_ATTRIBUTE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/ProducesRequestCondition$ProduceMediaTypeExpression.class */
    public static class ProduceMediaTypeExpression extends AbstractMediaTypeExpression {
        ProduceMediaTypeExpression(MediaType mediaType, boolean negated) {
            super(mediaType, negated);
        }

        ProduceMediaTypeExpression(String expression) {
            super(expression);
        }

        public final boolean match(List<MediaType> acceptedMediaTypes) {
            boolean match = matchMediaType(acceptedMediaTypes);
            return (!isNegated()) == match;
        }

        private boolean matchMediaType(List<MediaType> acceptedMediaTypes) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                if (getMediaType().isCompatibleWith(acceptedMediaType) && matchParameters(acceptedMediaType)) {
                    return true;
                }
            }
            return false;
        }

        private boolean matchParameters(MediaType acceptedMediaType) {
            for (String name : getMediaType().getParameters().keySet()) {
                String s1 = getMediaType().getParameter(name);
                String s2 = acceptedMediaType.getParameter(name);
                if (StringUtils.hasText(s1) && StringUtils.hasText(s2) && !s1.equalsIgnoreCase(s2)) {
                    return false;
                }
            }
            return true;
        }
    }
}
