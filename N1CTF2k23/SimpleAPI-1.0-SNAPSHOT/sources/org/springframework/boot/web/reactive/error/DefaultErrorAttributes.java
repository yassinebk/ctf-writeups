package org.springframework.boot.web.reactive.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.tags.BindErrorsTag;
import org.springframework.web.servlet.tags.BindTag;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/error/DefaultErrorAttributes.class */
public class DefaultErrorAttributes implements ErrorAttributes {
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";
    private final Boolean includeException;

    public DefaultErrorAttributes() {
        this.includeException = null;
    }

    @Deprecated
    public DefaultErrorAttributes(boolean includeException) {
        this.includeException = Boolean.valueOf(includeException);
    }

    @Override // org.springframework.boot.web.reactive.error.ErrorAttributes
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, options.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE));
        if (this.includeException != null) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (!options.isIncluded(ErrorAttributeOptions.Include.EXCEPTION)) {
            errorAttributes.remove(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE);
        }
        if (!options.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE)) {
            errorAttributes.remove("trace");
        }
        if (!options.isIncluded(ErrorAttributeOptions.Include.MESSAGE) && errorAttributes.get("message") != null) {
            errorAttributes.put("message", "");
        }
        if (!options.isIncluded(ErrorAttributeOptions.Include.BINDING_ERRORS)) {
            errorAttributes.remove(BindErrorsTag.ERRORS_VARIABLE_NAME);
        }
        return errorAttributes;
    }

    @Override // org.springframework.boot.web.reactive.error.ErrorAttributes
    @Deprecated
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("path", request.path());
        Throwable error = getError(request);
        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations.from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
        HttpStatus errorStatus = determineHttpStatus(error, responseStatusAnnotation);
        errorAttributes.put(BindTag.STATUS_VARIABLE_NAME, Integer.valueOf(errorStatus.value()));
        errorAttributes.put("error", errorStatus.getReasonPhrase());
        errorAttributes.put("message", determineMessage(error, responseStatusAnnotation));
        errorAttributes.put("requestId", request.exchange().getRequest().getId());
        handleException(errorAttributes, determineException(error), includeStackTrace);
        return errorAttributes;
    }

    private HttpStatus determineHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getStatus();
        }
        return (HttpStatus) responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String determineMessage(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof BindingResult) {
            return error.getMessage();
        }
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getReason();
        }
        String reason = (String) responseStatusAnnotation.getValue("reason", String.class).orElse("");
        if (StringUtils.hasText(reason)) {
            return reason;
        }
        return error.getMessage() != null ? error.getMessage() : "";
    }

    private Throwable determineException(Throwable error) {
        if (error instanceof ResponseStatusException) {
            return error.getCause() != null ? error.getCause() : error;
        }
        return error;
    }

    private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorAttributes.put("trace", stackTrace.toString());
    }

    private void handleException(Map<String, Object> errorAttributes, Throwable error, boolean includeStackTrace) {
        errorAttributes.put(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE, error.getClass().getName());
        if (includeStackTrace) {
            addStackTrace(errorAttributes, error);
        }
        if (error instanceof BindingResult) {
            BindingResult result = (BindingResult) error;
            if (result.hasErrors()) {
                errorAttributes.put(BindErrorsTag.ERRORS_VARIABLE_NAME, result.getAllErrors());
            }
        }
    }

    @Override // org.springframework.boot.web.reactive.error.ErrorAttributes
    public Throwable getError(ServerRequest request) {
        return (Throwable) request.attribute(ERROR_ATTRIBUTE).orElseThrow(() -> {
            return new IllegalStateException("Missing exception attribute in ServerWebExchange");
        });
    }

    @Override // org.springframework.boot.web.reactive.error.ErrorAttributes
    public void storeErrorInformation(Throwable error, ServerWebExchange exchange) {
        exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE, error);
    }
}
