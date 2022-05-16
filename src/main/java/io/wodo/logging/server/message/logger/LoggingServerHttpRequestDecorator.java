package io.wodo.logging.server.message.logger;

import io.wodo.logging.LoggingProperties;
import io.wodo.logging.provider.BodyProvider;
import io.wodo.logging.provider.ReqIdProvider;
import io.wodo.logging.server.exception.DataBufferCopyingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.FastByteArrayOutputStream;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.channels.Channels;

@Log4j2
public class LoggingServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private final BodyProvider bodyProvider = new BodyProvider();
    private final String reqIdMessage;


    public LoggingServerHttpRequestDecorator(ServerHttpRequest delegate, LoggingProperties loggingProperties) {
        super(delegate);
        reqIdMessage = new ReqIdProvider().createFromLogId(delegate.getId(), loggingProperties);
    }


    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody()
                .switchIfEmpty(Flux.<DataBuffer>empty()
                                       .doOnComplete(() -> log.info(reqIdMessage + bodyProvider.createNoBodyMessage())))

                .doOnNext(dataBuffer -> {
                    String fullBodyMessage = bodyProvider.createBodyMessage(copyBodyBuffer(dataBuffer));
                    log.info(reqIdMessage + fullBodyMessage);
                });
    }


    private FastByteArrayOutputStream copyBodyBuffer(DataBuffer buffer) {
        try {
            FastByteArrayOutputStream bodyStream = new FastByteArrayOutputStream();
            Channels.newChannel(bodyStream)
                    .write(buffer.asByteBuffer().asReadOnlyBuffer());

            return bodyStream;

        } catch (IOException e) {
            throw new DataBufferCopyingException(e);
        }
    }
}