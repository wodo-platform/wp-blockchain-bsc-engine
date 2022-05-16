package io.wodo.logging.provider;

import io.wodo.logging.LoggingUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Log4j2
public final class BodyProvider {

    public Mono<String> createBodyMessage(Mono<String> bodyMono) {
        return bodyMono
                .defaultIfEmpty(LoggingUtils.NO_BODY_MESSAGE)
                .map(this::createBodyMessage);
    }

    public String createBodyMessage(FastByteArrayOutputStream bodyOutputStream) {
        return createBodyMessage(bodyOutputStream.toString());
    }

    public String createBodyMessage(DataBuffer bodyDataBuffer) {
        return createBodyMessage(bodyDataBuffer.toString(Charset.defaultCharset()));
    }

    public String createBodyMessage(String body) {
        return StringUtils.hasLength(body) ? create(body) : createNoBodyMessage();
    }

    public String createNoBodyMessage() {
        return create(LoggingUtils.NO_BODY_MESSAGE);
    }

    private String create(String body) {

        if (LoggingUtils.NO_BODY_MESSAGE.equals(body)) {
            return (" BODY: [ " + body) + " ]";
        } else {
            StringBuilder singleLineJson = new StringBuilder();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(body);
            } catch (JSONException e) {
                log.error("Could not parse json body to JSONObject");
            }
            singleLineJson.append(jsonObject);
            return (" BODY: [ " + singleLineJson) + " ]";
        }
    }
}