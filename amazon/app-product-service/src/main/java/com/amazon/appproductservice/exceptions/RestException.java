package com.amazon.appproductservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public RestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public RestException(String message) {
        super(message);
    }

}
