package io.wodo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class UnexpectedVersionException extends NotFoundException {

    public UnexpectedVersionException(String entity, Long expectedVersion, Long foundVersion) {
        super(String.format("[%s] has a different version than the expected one. Expected [%s], found [%s]",
                            entity, expectedVersion, foundVersion));
    }

}
