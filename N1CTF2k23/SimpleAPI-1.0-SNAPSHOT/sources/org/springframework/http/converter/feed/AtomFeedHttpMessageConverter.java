package org.springframework.http.converter.feed;

import com.rometools.rome.feed.atom.Feed;
import org.springframework.http.MediaType;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/converter/feed/AtomFeedHttpMessageConverter.class */
public class AtomFeedHttpMessageConverter extends AbstractWireFeedHttpMessageConverter<Feed> {
    public AtomFeedHttpMessageConverter() {
        super(new MediaType("application", "atom+xml"));
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        return Feed.class.isAssignableFrom(clazz);
    }
}
