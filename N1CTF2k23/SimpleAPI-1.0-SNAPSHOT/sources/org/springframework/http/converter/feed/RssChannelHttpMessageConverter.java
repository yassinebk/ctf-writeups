package org.springframework.http.converter.feed;

import com.rometools.rome.feed.rss.Channel;
import org.springframework.http.MediaType;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/converter/feed/RssChannelHttpMessageConverter.class */
public class RssChannelHttpMessageConverter extends AbstractWireFeedHttpMessageConverter<Channel> {
    public RssChannelHttpMessageConverter() {
        super(MediaType.APPLICATION_RSS_XML);
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        return Channel.class.isAssignableFrom(clazz);
    }
}
