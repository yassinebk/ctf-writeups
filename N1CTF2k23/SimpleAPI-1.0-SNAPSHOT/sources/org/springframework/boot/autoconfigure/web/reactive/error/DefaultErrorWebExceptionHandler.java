package org.springframework.boot.autoconfigure.web.reactive.error;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.tags.BindTag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/error/DefaultErrorWebExceptionHandler.class */
public class DefaultErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private static final MediaType TEXT_HTML_UTF8 = new MediaType("text", "html", StandardCharsets.UTF_8);
    private static final Map<HttpStatus.Series, String> SERIES_VIEWS;
    private final ErrorProperties errorProperties;

    static {
        Map<HttpStatus.Series, String> views = new EnumMap<>(HttpStatus.Series.class);
        views.put(HttpStatus.Series.CLIENT_ERROR, "4xx");
        views.put(HttpStatus.Series.SERVER_ERROR, "5xx");
        SERIES_VIEWS = Collections.unmodifiableMap(views);
    }

    public DefaultErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, applicationContext);
        this.errorProperties = errorProperties;
    }

    @Override // org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(acceptsTextHtml(), this::renderErrorView).andRoute(RequestPredicates.all(), this::renderErrorResponse);
    }

    protected Mono<ServerResponse> renderErrorView(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        int errorStatus = getHttpStatus(error);
        ServerResponse.BodyBuilder responseBody = ServerResponse.status(errorStatus).contentType(TEXT_HTML_UTF8);
        return Flux.just(getData(errorStatus).toArray(new String[0])).flatMap(viewName -> {
            return renderErrorView(viewName, responseBody, error);
        }).switchIfEmpty(this.errorProperties.getWhitelabel().isEnabled() ? renderDefaultErrorView(responseBody, error) : Mono.error(getError(request))).next();
    }

    private List<String> getData(int errorStatus) {
        List<String> data = new ArrayList<>();
        data.add("error/" + errorStatus);
        HttpStatus.Series series = HttpStatus.Series.resolve(errorStatus);
        if (series != null) {
            data.add("error/" + SERIES_VIEWS.get(series));
        }
        data.add("error/error");
        return data;
    }

    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        return ServerResponse.status(getHttpStatus(error)).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(error));
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(ServerRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (isIncludeStackTrace(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        if (isIncludeMessage(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (isIncludeBindingErrors(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }
        return options;
    }

    protected boolean isIncludeStackTrace(ServerRequest request, MediaType produces) {
        switch (this.errorProperties.getIncludeStacktrace()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
            case ON_TRACE_PARAM:
                return isTraceEnabled(request);
            default:
                return false;
        }
    }

    protected boolean isIncludeMessage(ServerRequest request, MediaType produces) {
        switch (this.errorProperties.getIncludeMessage()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return isMessageEnabled(request);
            default:
                return false;
        }
    }

    protected boolean isIncludeBindingErrors(ServerRequest request, MediaType produces) {
        switch (this.errorProperties.getIncludeBindingErrors()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return isBindingErrorsEnabled(request);
            default:
                return false;
        }
    }

    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return ((Integer) errorAttributes.get(BindTag.STATUS_VARIABLE_NAME)).intValue();
    }

    protected RequestPredicate acceptsTextHtml() {
        return serverRequest -> {
            try {
                List<MediaType> acceptedMediaTypes = serverRequest.headers().accept();
                acceptedMediaTypes.remove(MediaType.ALL);
                MediaType.sortBySpecificityAndQuality(acceptedMediaTypes);
                Stream<MediaType> stream = acceptedMediaTypes.stream();
                MediaType mediaType = MediaType.TEXT_HTML;
                mediaType.getClass();
                return stream.anyMatch(this::isCompatibleWith);
            } catch (InvalidMediaTypeException e) {
                return false;
            }
        };
    }
}
