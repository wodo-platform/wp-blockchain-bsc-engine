package io.wodo.logging.server.message.logger;

import io.wodo.logging.LoggingProperties;
import io.wodo.logging.provider.HttpStatusProvider;
import io.wodo.logging.provider.TimeElapsedProvider;
import io.wodo.logging.server.message.formatter.ServerMetadataMessageFormatter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;

@Log4j2
public final class DefaultServerResponseLogger implements ServerResponseLogger {


    private final LoggingProperties properties;
    private final List<ServerMetadataMessageFormatter> messageFormatters;

    private final HttpStatusProvider statusProvider = new HttpStatusProvider();
    private final TimeElapsedProvider timeProvider = new TimeElapsedProvider();


    public DefaultServerResponseLogger(LoggingProperties properties,
                                       List<ServerMetadataMessageFormatter> messageFormatters) {
        this.properties = properties;
        this.messageFormatters = messageFormatters;
    }


    @Override
    public ServerHttpResponse log(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        Supplier<String> baseMessageSupplier = createMetadataMessage(exchange, exchangeStartTimeMillis);

        if (properties.isLogBody()) {
            return new LoggingServerHttpResponseDecorator(exchange.getResponse(), baseMessageSupplier);

        } else {
            exchange.getResponse()
                    .beforeCommit(() -> Mono.fromRunnable(() -> log.info(baseMessageSupplier.get())));
            return exchange.getResponse();
        }
    }


    private Supplier<String> createMetadataMessage(ServerWebExchange exchange, long exchangeStartTimeMillis) {
        return () -> {
            String status = statusProvider.createMessage(exchange.getResponse().getRawStatusCode());
            StringBuilder metadata = new StringBuilder(status);

            for (ServerMetadataMessageFormatter formatter : messageFormatters) {
                metadata.append(formatter.formatMessage(exchange, properties));
            }

            String timeElapsed = timeProvider.createMessage(System.currentTimeMillis() - exchangeStartTimeMillis);

            return ("RESPONSE:" + timeElapsed) + metadata;
        };
    }
}