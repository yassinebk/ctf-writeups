package org.springframework.boot.web.servlet.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.tags.BindErrorsTag;
import org.springframework.web.servlet.tags.BindTag;
@Order(Integer.MIN_VALUE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/error/DefaultErrorAttributes.class */
public class DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered {
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";
    private final Boolean includeException;

    public DefaultErrorAttributes() {
        this.includeException = null;
    }

    @Deprecated
    public DefaultErrorAttributes(boolean includeException) {
        this.includeException = Boolean.valueOf(includeException);
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override // org.springframework.web.servlet.HandlerExceptionResolver
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        storeErrorAttributes(request, ex);
        return null;
    }

    private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
        request.setAttribute(ERROR_ATTRIBUTE, ex);
    }

    @Override // org.springframework.boot.web.servlet.error.ErrorAttributes
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = getErrorAttributes(webRequest, options.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE));
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

    @Override // org.springframework.boot.web.servlet.error.ErrorAttributes
    @Deprecated
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        addStatus(errorAttributes, webRequest);
        addErrorDetails(errorAttributes, webRequest, includeStackTrace);
        addPath(errorAttributes, webRequest);
        return errorAttributes;
    }

    private void addStatus(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        Integer status = (Integer) getAttribute(requestAttributes, "javax.servlet.error.status_code");
        if (status == null) {
            errorAttributes.put(BindTag.STATUS_VARIABLE_NAME, 999);
            errorAttributes.put("error", "None");
            return;
        }
        errorAttributes.put(BindTag.STATUS_VARIABLE_NAME, status);
        try {
            errorAttributes.put("error", HttpStatus.valueOf(status.intValue()).getReasonPhrase());
        } catch (Exception e) {
            errorAttributes.put("error", "Http Status " + status);
        }
    }

    private void addErrorDetails(Map<String, Object> errorAttributes, WebRequest webRequest, boolean includeStackTrace) {
        Throwable error = getError(webRequest);
        if (error != null) {
            while ((error instanceof ServletException) && error.getCause() != null) {
                error = error.getCause();
            }
            errorAttributes.put(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE, error.getClass().getName());
            if (includeStackTrace) {
                addStackTrace(errorAttributes, error);
            }
        }
        addErrorMessage(errorAttributes, webRequest, error);
    }

    private void addErrorMessage(Map<String, Object> errorAttributes, WebRequest webRequest, Throwable error) {
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            addExceptionErrorMessage(errorAttributes, webRequest, error);
        } else {
            addBindingResultErrorMessage(errorAttributes, result);
        }
    }

    private void addExceptionErrorMessage(Map<String, Object> errorAttributes, WebRequest webRequest, Throwable error) {
        Object message = getAttribute(webRequest, "javax.servlet.error.message");
        if (StringUtils.isEmpty(message) && error != null) {
            message = error.getMessage();
        }
        if (StringUtils.isEmpty(message)) {
            message = "No message available";
        }
        errorAttributes.put("message", message);
    }

    private void addBindingResultErrorMessage(Map<String, Object> errorAttributes, BindingResult result) {
        errorAttributes.put("message", "Validation failed for object='" + result.getObjectName() + "'. Error count: " + result.getErrorCount());
        errorAttributes.put(BindErrorsTag.ERRORS_VARIABLE_NAME, result.getAllErrors());
    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

    private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorAttributes.put("trace", stackTrace.toString());
    }

    private void addPath(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        String path = (String) getAttribute(requestAttributes, "javax.servlet.error.request_uri");
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

    @Override // org.springframework.boot.web.servlet.error.ErrorAttributes
    public Throwable getError(WebRequest webRequest) {
        Throwable exception = (Throwable) getAttribute(webRequest, ERROR_ATTRIBUTE);
        return exception != null ? exception : (Throwable) getAttribute(webRequest, "javax.servlet.error.exception");
    }

    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, 0);
    }
}
