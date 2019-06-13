package com.stackroute.usertrackservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyRegisteredException extends Exception {
    public UserAlreadyRegisteredException(){}
    public UserAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
