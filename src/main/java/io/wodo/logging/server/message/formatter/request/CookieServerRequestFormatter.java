package io.wodo.logging.server.message.formatter.request;

import io.wodo.logging.LoggingProperties;
import io.wodo.logging.provider.CookieProvider;
import io.wodo.logging.server.message.formatter.ServerMetadataMessageFormatter;
import org.springframework.web.server.ServerWebExchange;

public final class CookieServerRequestFormatter implements ServerMetadataMessageFormatter {

    private final CookieProvider provider = new CookieProvider();


    @Override
    public String formatMessage(ServerWebExchange exchange, LoggingProperties properties) {
        return provider.createServerRequestMessage(exchange.getRequest().getCookies(), properties);
    }
}