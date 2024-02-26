package com.kbtg.bootcamp.posttest.exception;

import org.springframework.http.HttpStatus;

public class SetterMessageAndStatusCodeException extends RuntimeException {


    private final HttpStatus status;

    public SetterMessageAndStatusCodeException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
