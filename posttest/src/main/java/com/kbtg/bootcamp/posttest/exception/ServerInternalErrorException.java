package com.kbtg.bootcamp.posttest.exception;

public class ServerInternalErrorException extends RuntimeException{
    public ServerInternalErrorException(String message) {
        super(message);
    }

    public ServerInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
