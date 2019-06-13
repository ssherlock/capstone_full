package com.stackroute.usertrackservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class TrackAlreadyExistsException extends Exception {
    public TrackAlreadyExistsException(){}
    public TrackAlreadyExistsException(String msg) {
        super(msg);
    }
}
