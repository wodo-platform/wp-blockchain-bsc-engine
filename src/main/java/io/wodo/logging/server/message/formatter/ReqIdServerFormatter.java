package io.wodo.logging.server.message.formatter;

import io.wodo.logging.LoggingProperties;
import io.wodo.logging.provider.ReqIdProvider;
import org.springframework.web.server.ServerWebExchange;

public final class ReqIdServerFormatter implements ServerMetadataMessageFormatter {

    private final ReqIdProvider provider = new ReqIdProvider();


    @Override
    public String formatMessage(ServerWebExchange exchange, LoggingProperties properties) {
        return provider.createFromLogPrefix(exchange.getLogPrefix(), properties);
    }
}