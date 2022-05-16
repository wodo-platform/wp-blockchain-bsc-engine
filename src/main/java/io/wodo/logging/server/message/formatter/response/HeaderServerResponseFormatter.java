package io.wodo.logging.server.message.formatter.response;

import io.wodo.logging.LoggingProperties;
import io.wodo.logging.provider.HeaderProvider;
import io.wodo.logging.server.message.formatter.ServerMetadataMessageFormatter;
import org.springframework.web.server.ServerWebExchange;

public final class HeaderServerResponseFormatter implements ServerMetadataMessageFormatter {

    private final HeaderProvider provider = new HeaderProvider();


    @Override
    public String formatMessage(ServerWebExchange exchange, LoggingProperties properties) {
        return provider.createMessage(exchange.getResponse().getHeaders(), properties);
    }
}