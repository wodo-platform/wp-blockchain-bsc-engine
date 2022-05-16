package io.wodo.logging.server.exception;

public class DataBufferCopyingException extends RuntimeException {

    public DataBufferCopyingException(Throwable cause) {
        super("Failed to copy DataBuffer body!", cause);
    }
}