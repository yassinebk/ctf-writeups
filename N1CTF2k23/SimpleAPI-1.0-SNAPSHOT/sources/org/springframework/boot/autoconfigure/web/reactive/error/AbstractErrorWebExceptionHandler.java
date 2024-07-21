package org.springframework.boot.autoconfigure.web.reactive.error;

import ch.qos.logback.classic.spi.CallerData;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpLogging;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.tags.BindErrorsTag;
import org.springframework.web.servlet.tags.BindTag;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/error/AbstractErrorWebExceptionHandler.class */
public abstract class AbstractErrorWebExceptionHandler implements ErrorWebExceptionHandler, InitializingBean {
    private static final Set<String> DISCONNECTED_CLIENT_EXCEPTIONS;
    private static final Log logger;
    private final ApplicationContext applicationContext;
    private final ErrorAttributes errorAttributes;
    private final ResourceProperties resourceProperties;
    private final TemplateAvailabilityProviders templateAvailabilityProviders;
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    protected abstract RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes);

    static {
        Set<String> exceptions = new HashSet<>();
        exceptions.add("AbortedException");
        exceptions.add("ClientAbortException");
        exceptions.add("EOFException");
        exceptions.add("EofException");
        DISCONNECTED_CLIENT_EXCEPTIONS = Collections.unmodifiableSet(exceptions);
        logger = HttpLogging.forLogName(AbstractErrorWebExceptionHandler.class);
    }

    public AbstractErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        Assert.notNull(resourceProperties, "ResourceProperties must not be null");
        Assert.notNull(applicationContext, "ApplicationContext must not be null");
        this.errorAttributes = errorAttributes;
        this.resourceProperties = resourceProperties;
        this.applicationContext = applicationContext;
        this.templateAvailabilityProviders = new TemplateAvailabilityProviders(applicationContext);
    }

    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    @Deprecated
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        return getErrorAttributes(request, includeStackTrace ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE) : ErrorAttributeOptions.defaults());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        return this.errorAttributes.getErrorAttributes(request, options);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Throwable getError(ServerRequest request) {
        return this.errorAttributes.getError(request);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isTraceEnabled(ServerRequest request) {
        return getBooleanParameter(request, "trace");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isMessageEnabled(ServerRequest request) {
        return getBooleanParameter(request, "message");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isBindingErrorsEnabled(ServerRequest request) {
        return getBooleanParameter(request, BindErrorsTag.ERRORS_VARIABLE_NAME);
    }

    private boolean getBooleanParameter(ServerRequest request, String parameterName) {
        String parameter = (String) request.queryParam(parameterName).orElse("false");
        return !"false".equalsIgnoreCase(parameter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Mono<ServerResponse> renderErrorView(String viewName, ServerResponse.BodyBuilder responseBody, Map<String, Object> error) {
        if (isTemplateAvailable(viewName)) {
            return responseBody.render(viewName, error);
        }
        Resource resource = resolveResource(viewName);
        if (resource != null) {
            return responseBody.body(BodyInserters.fromResource(resource));
        }
        return Mono.empty();
    }

    private boolean isTemplateAvailable(String viewName) {
        return this.templateAvailabilityProviders.getProvider(viewName, this.applicationContext) != null;
    }

    private Resource resolveResource(String viewName) {
        String[] staticLocations;
        Resource resource;
        for (String location : this.resourceProperties.getStaticLocations()) {
            try {
                resource = this.applicationContext.getResource(location).createRelative(viewName + ThymeleafProperties.DEFAULT_SUFFIX);
            } catch (Exception e) {
            }
            if (resource.exists()) {
                return resource;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Mono<ServerResponse> renderDefaultErrorView(ServerResponse.BodyBuilder responseBody, Map<String, Object> error) {
        StringBuilder builder = new StringBuilder();
        Date timestamp = (Date) error.get("timestamp");
        Object message = error.get("message");
        Object trace = error.get("trace");
        Object requestId = error.get("requestId");
        builder.append("<html><body><h1>Whitelabel Error Page</h1>").append("<p>This application has no configured error view, so you are seeing this as a fallback.</p>").append("<div id='created'>").append(timestamp).append("</div>").append("<div>[").append(requestId).append("] There was an unexpected error (type=").append(htmlEscape(error.get("error"))).append(", status=").append(htmlEscape(error.get(BindTag.STATUS_VARIABLE_NAME))).append(").</div>");
        if (message != null) {
            builder.append("<div>").append(htmlEscape(message)).append("</div>");
        }
        if (trace != null) {
            builder.append("<div style='white-space:pre-wrap;'>").append(htmlEscape(trace)).append("</div>");
        }
        builder.append("</body></html>");
        return responseBody.bodyValue(builder.toString());
    }

    private String htmlEscape(Object input) {
        if (input != null) {
            return HtmlUtils.htmlEscape(input.toString());
        }
        return null;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(this.messageWriters)) {
            throw new IllegalArgumentException("Property 'messageWriters' is required");
        }
    }

    @Override // org.springframework.web.server.WebExceptionHandler
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        if (exchange.getResponse().isCommitted() || isDisconnectedClientError(throwable)) {
            return Mono.error(throwable);
        }
        this.errorAttributes.storeErrorInformation(throwable, exchange);
        ServerRequest request = ServerRequest.create(exchange, this.messageReaders);
        return getRoutingFunction(this.errorAttributes).route(request).switchIfEmpty(Mono.error(throwable)).flatMap(handler -> {
            return handler.handle(request);
        }).doOnNext(response -> {
            logError(request, response, throwable);
        }).flatMap(response2 -> {
            return write(exchange, response2);
        });
    }

    private boolean isDisconnectedClientError(Throwable ex) {
        return DISCONNECTED_CLIENT_EXCEPTIONS.contains(ex.getClass().getSimpleName()) || isDisconnectedClientErrorMessage(NestedExceptionUtils.getMostSpecificCause(ex).getMessage());
    }

    private boolean isDisconnectedClientErrorMessage(String message) {
        String message2 = message != null ? message.toLowerCase() : "";
        return message2.contains("broken pipe") || message2.contains("connection reset by peer");
    }

    protected void logError(ServerRequest request, ServerResponse response, Throwable throwable) {
        if (logger.isDebugEnabled()) {
            logger.debug(request.exchange().getLogPrefix() + formatError(throwable, request));
        }
        if (HttpStatus.resolve(response.rawStatusCode()) != null && response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            logger.error(LogMessage.of(() -> {
                return String.format("%s 500 Server Error for %s", request.exchange().getLogPrefix(), formatRequest(request));
            }), throwable);
        }
    }

    private String formatError(Throwable ex, ServerRequest request) {
        String reason = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        return "Resolved [" + reason + "] for HTTP " + request.methodName() + " " + request.path();
    }

    private String formatRequest(ServerRequest request) {
        String rawQuery = request.uri().getRawQuery();
        String query = StringUtils.hasText(rawQuery) ? CallerData.NA + rawQuery : "";
        return "HTTP " + request.methodName() + " \"" + request.path() + query + "\"";
    }

    private Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
        exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/error/AbstractErrorWebExceptionHandler$ResponseContext.class */
    public class ResponseContext implements ServerResponse.Context {
        private ResponseContext() {
        }

        public List<HttpMessageWriter<?>> messageWriters() {
            return AbstractErrorWebExceptionHandler.this.messageWriters;
        }

        public List<ViewResolver> viewResolvers() {
            return AbstractErrorWebExceptionHandler.this.viewResolvers;
        }
    }
}
