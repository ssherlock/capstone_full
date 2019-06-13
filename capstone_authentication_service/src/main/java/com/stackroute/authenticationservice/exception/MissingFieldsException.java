package com.stackroute.authenticationservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(code= BAD_REQUEST)
public class MissingFieldsException extends Exception {

    public MissingFieldsException(){}
    public MissingFieldsException(String msg){
        super(msg);
    }
}
