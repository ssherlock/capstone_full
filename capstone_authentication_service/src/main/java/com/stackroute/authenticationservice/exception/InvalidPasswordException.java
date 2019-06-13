package com.stackroute.authenticationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.UNAUTHORIZED)
public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(){}
    public InvalidPasswordException(String msg){
        super(msg);
    }
}
