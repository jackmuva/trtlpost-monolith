package com.jackmu.exception;

import org.springframework.http.HttpStatus;

public class SlowCapsuleAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public SlowCapsuleAPIException(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
