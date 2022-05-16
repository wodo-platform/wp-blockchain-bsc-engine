package io.wodo.logging.server.message.logger;

import io.wodo.logging.LoggingProperties;
import io.wodo.logging.server.message.formatter.ServerMetadataMessageFormatter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Log4j2
public final class DefaultServerRequestLogger implements ServerRequestLogger {

    private final LoggingProperties properties;
    private final List<ServerMetadataMessageFormatter> messageFormatters;


    public DefaultServerRequestLogger(LoggingProperties properties,
                                      List<ServerMetadataMessageFormatter> messageFormatters) {
        this.properties = properties;
        this.messageFormatters = messageFormatters;
    }


    @Override
    public ServerHttpRequest log(ServerWebExchange exchange) {
        StringBuilder metadata = new StringBuilder("REQUEST: ")
                .append(exchange.getRequest().getMethodValue())
                .append(" ")
                .append(exchange.getRequest().getURI());

        for (ServerMetadataMessageFormatter formatter : messageFormatters) {
            metadata.append(formatter.formatMessage(exchange, properties));
        }

        log.info(metadata.toString());

        return properties.isLogBody()
                ? new LoggingServerHttpRequestDecorator(exchange.getRequest(), properties)
                : exchange.getRequest();
    }
}