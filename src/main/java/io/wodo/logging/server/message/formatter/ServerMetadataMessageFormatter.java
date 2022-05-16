package io.wodo.logging.server.message.formatter;

import io.wodo.logging.LoggingProperties;
import org.springframework.web.server.ServerWebExchange;

public interface ServerMetadataMessageFormatter {

    String formatMessage(ServerWebExchange exchange, LoggingProperties properties);
}
