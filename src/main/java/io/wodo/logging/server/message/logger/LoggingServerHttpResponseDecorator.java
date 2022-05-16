package io.wodo.logging.server.message.logger;

import io.wodo.logging.provider.BodyProvider;
import io.wodo.logging.server.exception.DataBufferCopyingException;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.FastByteArrayOutputStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.channels.Channels;
import java.util.function.Supplier;

@Log4j2
public class LoggingServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private final BodyProvider provider = new BodyProvider();
    private final FastByteArrayOutputStream bodyOutputStream = new FastByteArrayOutputStream();


    public LoggingServerHttpResponseDecorator(ServerHttpResponse delegate, Supplier<String> sourceLogMessage) {
        super(delegate);

        delegate.beforeCommit(() -> {
            String bodyMessage = provider.createBodyMessage(bodyOutputStream);
            String fullLogMessage = sourceLogMessage.get() + bodyMessage;

            log.info(fullLogMessage);

            return Mono.empty();
        });
    }


    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> bodyBufferWrapper = Flux.from(body)
                .map(dataBuffer -> copyBodyBuffer(bodyOutputStream, dataBuffer));

        return super.writeWith(bodyBufferWrapper);
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        Flux<Flux<DataBuffer>> bodyBufferWrapper = Flux.from(body)
                .map(publisher -> Flux.from(publisher)
                        .map(buffer -> copyBodyBuffer(bodyOutputStream, buffer)));

        return super.writeAndFlushWith(bodyBufferWrapper);
    }


    private DataBuffer copyBodyBuffer(FastByteArrayOutputStream bodyStream, DataBuffer buffer) {
        try {
            Channels.newChannel(bodyStream)
                    .write(buffer.asByteBuffer().asReadOnlyBuffer());

            return buffer;

        } catch (IOException e) {
            throw new DataBufferCopyingException(e);
        }
    }
}