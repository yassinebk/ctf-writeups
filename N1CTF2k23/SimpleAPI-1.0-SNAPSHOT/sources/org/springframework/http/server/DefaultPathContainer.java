package org.springframework.http.server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.server.PathContainer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/DefaultPathContainer.class */
final class DefaultPathContainer implements PathContainer {
    private static final MultiValueMap<String, String> EMPTY_PARAMS = new LinkedMultiValueMap();
    private static final PathContainer EMPTY_PATH = new DefaultPathContainer("", Collections.emptyList());
    private static final Map<Character, DefaultSeparator> SEPARATORS = new HashMap(2);
    private final String path;
    private final List<PathContainer.Element> elements;

    static {
        SEPARATORS.put('/', new DefaultSeparator('/', "%2F"));
        SEPARATORS.put('.', new DefaultSeparator('.', "%2E"));
    }

    private DefaultPathContainer(String path, List<PathContainer.Element> elements) {
        this.path = path;
        this.elements = Collections.unmodifiableList(elements);
    }

    @Override // org.springframework.http.server.PathContainer
    public String value() {
        return this.path;
    }

    @Override // org.springframework.http.server.PathContainer
    public List<PathContainer.Element> elements() {
        return this.elements;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PathContainer)) {
            return false;
        }
        return value().equals(((PathContainer) other).value());
    }

    public int hashCode() {
        return this.path.hashCode();
    }

    public String toString() {
        return value();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PathContainer createFromUrlPath(String path, PathContainer.Options options) {
        int begin;
        if (path.isEmpty()) {
            return EMPTY_PATH;
        }
        char separator = options.separator();
        DefaultSeparator separatorElement = SEPARATORS.get(Character.valueOf(separator));
        if (separatorElement == null) {
            throw new IllegalArgumentException("Unexpected separator: '" + separator + "'");
        }
        List<PathContainer.Element> elements = new ArrayList<>();
        if (path.charAt(0) == separator) {
            begin = 1;
            elements.add(separatorElement);
        } else {
            begin = 0;
        }
        while (begin < path.length()) {
            int end = path.indexOf(separator, begin);
            String segment = end != -1 ? path.substring(begin, end) : path.substring(begin);
            if (!segment.isEmpty()) {
                elements.add(options.shouldDecodeAndParseSegments() ? decodeAndParsePathSegment(segment) : new DefaultPathSegment(segment, separatorElement));
            }
            if (end == -1) {
                break;
            }
            elements.add(separatorElement);
            begin = end + 1;
        }
        return new DefaultPathContainer(path, elements);
    }

    private static PathContainer.PathSegment decodeAndParsePathSegment(String segment) {
        Charset charset = StandardCharsets.UTF_8;
        int index = segment.indexOf(59);
        if (index == -1) {
            String valueToMatch = StringUtils.uriDecode(segment, charset);
            return new DefaultPathSegment(segment, valueToMatch, EMPTY_PARAMS);
        }
        String valueToMatch2 = StringUtils.uriDecode(segment.substring(0, index), charset);
        String pathParameterContent = segment.substring(index);
        MultiValueMap<String, String> parameters = parsePathParams(pathParameterContent, charset);
        return new DefaultPathSegment(segment, valueToMatch2, parameters);
    }

    private static MultiValueMap<String, String> parsePathParams(String input, Charset charset) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        int i = 1;
        while (true) {
            int begin = i;
            if (begin >= input.length()) {
                break;
            }
            int end = input.indexOf(59, begin);
            String param = end != -1 ? input.substring(begin, end) : input.substring(begin);
            parsePathParamValues(param, charset, result);
            if (end == -1) {
                break;
            }
            i = end + 1;
        }
        return result;
    }

    private static void parsePathParamValues(String input, Charset charset, MultiValueMap<String, String> output) {
        String[] commaDelimitedListToStringArray;
        if (StringUtils.hasText(input)) {
            int index = input.indexOf(61);
            if (index != -1) {
                String name = input.substring(0, index);
                String value = input.substring(index + 1);
                for (String v : StringUtils.commaDelimitedListToStringArray(value)) {
                    name = StringUtils.uriDecode(name, charset);
                    if (StringUtils.hasText(name)) {
                        output.add(name, StringUtils.uriDecode(v, charset));
                    }
                }
                return;
            }
            String name2 = StringUtils.uriDecode(input, charset);
            if (StringUtils.hasText(name2)) {
                output.add(input, "");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PathContainer subPath(PathContainer container, int fromIndex, int toIndex) {
        List<PathContainer.Element> elements = container.elements();
        if (fromIndex == 0 && toIndex == elements.size()) {
            return container;
        }
        if (fromIndex == toIndex) {
            return EMPTY_PATH;
        }
        Assert.isTrue(fromIndex >= 0 && fromIndex < elements.size(), () -> {
            return "Invalid fromIndex: " + fromIndex;
        });
        Assert.isTrue(toIndex >= 0 && toIndex <= elements.size(), () -> {
            return "Invalid toIndex: " + toIndex;
        });
        Assert.isTrue(fromIndex < toIndex, () -> {
            return "fromIndex: " + fromIndex + " should be < toIndex " + toIndex;
        });
        List<PathContainer.Element> subList = elements.subList(fromIndex, toIndex);
        String path = (String) subList.stream().map((v0) -> {
            return v0.value();
        }).collect(Collectors.joining(""));
        return new DefaultPathContainer(path, subList);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/DefaultPathContainer$DefaultSeparator.class */
    private static class DefaultSeparator implements PathContainer.Separator {
        private final String separator;
        private final String encodedSequence;

        DefaultSeparator(char separator, String encodedSequence) {
            this.separator = String.valueOf(separator);
            this.encodedSequence = encodedSequence;
        }

        @Override // org.springframework.http.server.PathContainer.Element
        public String value() {
            return this.separator;
        }

        public String encodedSequence() {
            return this.encodedSequence;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/DefaultPathContainer$DefaultPathSegment.class */
    public static class DefaultPathSegment implements PathContainer.PathSegment {
        private final String value;
        private final String valueToMatch;
        private final char[] valueToMatchAsChars;
        private final MultiValueMap<String, String> parameters;

        DefaultPathSegment(String value, String valueToMatch, MultiValueMap<String, String> params) {
            this.value = value;
            this.valueToMatch = valueToMatch;
            this.valueToMatchAsChars = valueToMatch.toCharArray();
            this.parameters = CollectionUtils.unmodifiableMultiValueMap(params);
        }

        DefaultPathSegment(String value, DefaultSeparator separator) {
            this.value = value;
            this.valueToMatch = value.contains(separator.encodedSequence()) ? value.replaceAll(separator.encodedSequence(), separator.value()) : value;
            this.valueToMatchAsChars = this.valueToMatch.toCharArray();
            this.parameters = DefaultPathContainer.EMPTY_PARAMS;
        }

        @Override // org.springframework.http.server.PathContainer.Element
        public String value() {
            return this.value;
        }

        @Override // org.springframework.http.server.PathContainer.PathSegment
        public String valueToMatch() {
            return this.valueToMatch;
        }

        @Override // org.springframework.http.server.PathContainer.PathSegment
        public char[] valueToMatchAsChars() {
            return this.valueToMatchAsChars;
        }

        @Override // org.springframework.http.server.PathContainer.PathSegment
        public MultiValueMap<String, String> parameters() {
            return this.parameters;
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PathContainer.PathSegment)) {
                return false;
            }
            return value().equals(((PathContainer.PathSegment) other).value());
        }

        public int hashCode() {
            return this.value.hashCode();
        }

        public String toString() {
            return "[value='" + this.value + "']";
        }
    }
}
