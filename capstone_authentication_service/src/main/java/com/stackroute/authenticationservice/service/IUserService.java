package com.stackroute.authenticationservice.service;


import com.stackroute.authenticationservice.exception.InvalidPasswordException;
import com.stackroute.authenticationservice.exception.MissingFieldsException;
import com.stackroute.authenticationservice.exception.UserAlreadyExistsException;
import com.stackroute.authenticationservice.exception.UserNotFoundException;
import com.stackroute.authenticationservice.model.User;

public interface IUserService {
    public User saveUser(final User user) throws UserAlreadyExistsException, MissingFieldsException;
    public User validateLogin(final User user) throws UserNotFoundException, InvalidPasswordException;
}
