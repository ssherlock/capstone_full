package com.stackroute.usertrackservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TrackNotFoundException extends Exception {
    public TrackNotFoundException(){}
    public TrackNotFoundException(String msg) {
        super(msg);
    }
}