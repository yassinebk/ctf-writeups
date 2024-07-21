package org.springframework.web.servlet.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.ServerResponse;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerResponseBuilder.class */
class DefaultServerResponseBuilder implements ServerResponse.BodyBuilder {
    private final int statusCode;
    private final HttpHeaders headers = new HttpHeaders();
    private final MultiValueMap<String, Cookie> cookies = new LinkedMultiValueMap();

    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public /* bridge */ /* synthetic */ ServerResponse.BodyBuilder allow(Set set) {
        return allow((Set<HttpMethod>) set);
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public /* bridge */ /* synthetic */ ServerResponse.BodyBuilder cookies(Consumer consumer) {
        return cookies((Consumer<MultiValueMap<String, Cookie>>) consumer);
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public /* bridge */ /* synthetic */ ServerResponse.BodyBuilder headers(Consumer consumer) {
        return headers((Consumer<HttpHeaders>) consumer);
    }

    public DefaultServerResponseBuilder(ServerResponse other) {
        Assert.notNull(other, "ServerResponse must not be null");
        this.statusCode = other instanceof AbstractServerResponse ? ((AbstractServerResponse) other).statusCode : other.statusCode().value();
        this.headers.addAll(other.headers());
        this.cookies.addAll(other.cookies());
    }

    public DefaultServerResponseBuilder(HttpStatus status) {
        Assert.notNull(status, "HttpStatus must not be null");
        this.statusCode = status.value();
    }

    public DefaultServerResponseBuilder(int statusCode) {
        this.statusCode = statusCode;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder header(String headerName, String... headerValues) {
        for (String headerValue : headerValues) {
            this.headers.add(headerName, headerValue);
        }
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder headers(Consumer<HttpHeaders> headersConsumer) {
        headersConsumer.accept(this.headers);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder cookie(Cookie cookie) {
        Assert.notNull(cookie, "Cookie must not be null");
        this.cookies.add(cookie.getName(), cookie);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder cookies(Consumer<MultiValueMap<String, Cookie>> cookiesConsumer) {
        cookiesConsumer.accept(this.cookies);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder allow(HttpMethod... allowedMethods) {
        this.headers.setAllow(new LinkedHashSet(Arrays.asList(allowedMethods)));
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder allow(Set<HttpMethod> allowedMethods) {
        this.headers.setAllow(allowedMethods);
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.BodyBuilder
    public ServerResponse.BodyBuilder contentLength(long contentLength) {
        this.headers.setContentLength(contentLength);
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.BodyBuilder
    public ServerResponse.BodyBuilder contentType(MediaType contentType) {
        this.headers.setContentType(contentType);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder eTag(String etag) {
        if (!etag.startsWith("\"") && !etag.startsWith("W/\"")) {
            etag = "\"" + etag;
        }
        if (!etag.endsWith("\"")) {
            etag = etag + "\"";
        }
        this.headers.setETag(etag);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder lastModified(ZonedDateTime lastModified) {
        this.headers.setLastModified(lastModified);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder lastModified(Instant lastModified) {
        this.headers.setLastModified(lastModified);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder location(URI location) {
        this.headers.setLocation(location);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder cacheControl(CacheControl cacheControl) {
        this.headers.setCacheControl(cacheControl);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse.BodyBuilder varyBy(String... requestHeaders) {
        this.headers.setVary(Arrays.asList(requestHeaders));
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse build() {
        return build(request, response -> {
            return null;
        });
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.HeadersBuilder
    public ServerResponse build(BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> writeFunction) {
        return new WriterFunctionResponse(this.statusCode, this.headers, this.cookies, writeFunction);
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.BodyBuilder
    public ServerResponse body(Object body) {
        return DefaultEntityResponseBuilder.fromObject(body).status(this.statusCode).headers(headers -> {
            headers.putAll(this.headers);
        }).cookies(cookies -> {
            cookies.addAll(this.cookies);
        }).build();
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.BodyBuilder
    public <T> ServerResponse body(T body, ParameterizedTypeReference<T> bodyType) {
        return DefaultEntityResponseBuilder.fromObject(body, bodyType).status(this.statusCode).headers(headers -> {
            headers.putAll(this.headers);
        }).cookies(cookies -> {
            cookies.addAll(this.cookies);
        }).build();
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.BodyBuilder
    public ServerResponse render(String name, Object... modelAttributes) {
        return new DefaultRenderingResponseBuilder(name).status(this.statusCode).headers(headers -> {
            headers.putAll(this.headers);
        }).cookies(cookies -> {
            cookies.addAll(this.cookies);
        }).modelAttributes(modelAttributes).build();
    }

    @Override // org.springframework.web.servlet.function.ServerResponse.BodyBuilder
    public ServerResponse render(String name, Map<String, ?> model) {
        return new DefaultRenderingResponseBuilder(name).status(this.statusCode).headers(headers -> {
            headers.putAll(this.headers);
        }).cookies(cookies -> {
            cookies.addAll(this.cookies);
        }).modelAttributes(model).build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerResponseBuilder$AbstractServerResponse.class */
    public static abstract class AbstractServerResponse implements ServerResponse {
        private static final Set<HttpMethod> SAFE_METHODS = EnumSet.of(HttpMethod.GET, HttpMethod.HEAD);
        final int statusCode;
        private final HttpHeaders headers;
        private final MultiValueMap<String, Cookie> cookies;
        private final List<ErrorHandler<?>> errorHandlers = new ArrayList();

        @Nullable
        protected abstract ModelAndView writeToInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ServerResponse.Context context) throws ServletException, IOException;

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractServerResponse(int statusCode, HttpHeaders headers, MultiValueMap<String, Cookie> cookies) {
            this.statusCode = statusCode;
            this.headers = HttpHeaders.readOnlyHttpHeaders(headers);
            this.cookies = CollectionUtils.unmodifiableMultiValueMap(new LinkedMultiValueMap(cookies));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public <T extends ServerResponse> void addErrorHandler(Predicate<Throwable> predicate, BiFunction<Throwable, ServerRequest, T> errorHandler) {
            Assert.notNull(predicate, "Predicate must not be null");
            Assert.notNull(errorHandler, "ErrorHandler must not be null");
            this.errorHandlers.add(new ErrorHandler<>(predicate, errorHandler));
        }

        @Override // org.springframework.web.servlet.function.ServerResponse
        public final HttpStatus statusCode() {
            return HttpStatus.valueOf(this.statusCode);
        }

        @Override // org.springframework.web.servlet.function.ServerResponse
        public int rawStatusCode() {
            return this.statusCode;
        }

        @Override // org.springframework.web.servlet.function.ServerResponse
        public final HttpHeaders headers() {
            return this.headers;
        }

        @Override // org.springframework.web.servlet.function.ServerResponse
        public MultiValueMap<String, Cookie> cookies() {
            return this.cookies;
        }

        @Override // org.springframework.web.servlet.function.ServerResponse
        public ModelAndView writeTo(HttpServletRequest request, HttpServletResponse response, ServerResponse.Context context) throws ServletException, IOException {
            try {
                writeStatusAndHeaders(response);
                long lastModified = headers().getLastModified();
                ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);
                HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
                if (SAFE_METHODS.contains(httpMethod) && servletWebRequest.checkNotModified(headers().getETag(), lastModified)) {
                    return null;
                }
                return writeToInternal(request, response, context);
            } catch (Throwable throwable) {
                return handleError(throwable, request, response, context);
            }
        }

        private void writeStatusAndHeaders(HttpServletResponse response) {
            response.setStatus(this.statusCode);
            writeHeaders(response);
            writeCookies(response);
        }

        private void writeHeaders(HttpServletResponse servletResponse) {
            this.headers.forEach(headerName, headerValues -> {
                Iterator it = headerValues.iterator();
                while (it.hasNext()) {
                    String headerValue = (String) it.next();
                    servletResponse.addHeader(headerName, headerValue);
                }
            });
            if (servletResponse.getContentType() == null && this.headers.getContentType() != null) {
                servletResponse.setContentType(this.headers.getContentType().toString());
            }
            if (servletResponse.getCharacterEncoding() == null && this.headers.getContentType() != null && this.headers.getContentType().getCharset() != null) {
                servletResponse.setCharacterEncoding(this.headers.getContentType().getCharset().name());
            }
        }

        private void writeCookies(HttpServletResponse servletResponse) {
            Stream<R> flatMap = this.cookies.values().stream().flatMap((v0) -> {
                return v0.stream();
            });
            servletResponse.getClass();
            flatMap.forEach(this::addCookie);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Nullable
        public ModelAndView handleError(Throwable t, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServerResponse.Context context) {
            return (ModelAndView) this.errorHandlers.stream().filter(errorHandler -> {
                return errorHandler.test(t);
            }).findFirst().map(errorHandler2 -> {
                ServerRequest serverRequest = (ServerRequest) servletRequest.getAttribute(RouterFunctions.REQUEST_ATTRIBUTE);
                ServerResponse serverResponse = errorHandler2.handle(t, serverRequest);
                try {
                    return serverResponse.writeTo(servletRequest, servletResponse, context);
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                } catch (ServletException ex2) {
                    throw new RuntimeException(ex2);
                }
            }).orElseThrow(() -> {
                return new RuntimeException(t);
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerResponseBuilder$AbstractServerResponse$ErrorHandler.class */
        public static class ErrorHandler<T extends ServerResponse> {
            private final Predicate<Throwable> predicate;
            private final BiFunction<Throwable, ServerRequest, T> responseProvider;

            public ErrorHandler(Predicate<Throwable> predicate, BiFunction<Throwable, ServerRequest, T> responseProvider) {
                Assert.notNull(predicate, "Predicate must not be null");
                Assert.notNull(responseProvider, "ResponseProvider must not be null");
                this.predicate = predicate;
                this.responseProvider = responseProvider;
            }

            public boolean test(Throwable t) {
                return this.predicate.test(t);
            }

            public T handle(Throwable t, ServerRequest serverRequest) {
                return this.responseProvider.apply(t, serverRequest);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerResponseBuilder$WriterFunctionResponse.class */
    public static class WriterFunctionResponse extends AbstractServerResponse {
        private final BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> writeFunction;

        public WriterFunctionResponse(int statusCode, HttpHeaders headers, MultiValueMap<String, Cookie> cookies, BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> writeFunction) {
            super(statusCode, headers, cookies);
            Assert.notNull(writeFunction, "WriteFunction must not be null");
            this.writeFunction = writeFunction;
        }

        @Override // org.springframework.web.servlet.function.DefaultServerResponseBuilder.AbstractServerResponse
        protected ModelAndView writeToInternal(HttpServletRequest request, HttpServletResponse response, ServerResponse.Context context) {
            return this.writeFunction.apply(request, response);
        }
    }
}
