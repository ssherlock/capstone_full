package com.stackroute.authenticationservice.controller;

import com.stackroute.authenticationservice.exception.InvalidPasswordException;
import com.stackroute.authenticationservice.exception.MissingFieldsException;
import com.stackroute.authenticationservice.exception.UserAlreadyExistsException;
import com.stackroute.authenticationservice.exception.UserNotFoundException;
import com.stackroute.authenticationservice.model.User;
import com.stackroute.authenticationservice.service.IUserService;
import com.stackroute.authenticationservice.service.SecurityTokenGeneratorImpl;
import org.mapstruct.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/authservice")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private IUserService userService;
    private SecurityTokenGeneratorImpl securityTokenService;
    private ResponseEntity<?> responseEntity;

    @Value("${exception.error_msg}")
    private String exceptionErrorMsg;
    @Value("${exception.userNotFound_msg}")
    private String exceptionUserNotFound;
    @Value("${exception.invalidPassword_msg}")
    private String exceptionInvalidPassword;
    @Value("${exception.userAlreadyExists_msg}")
    private String exceptionUserAlreadyExists;
    @Value("${exception.missingData_msg}")
    private String exceptionMissingData_msg;

    @Autowired
    public UserController(final IUserService userService,
                          final SecurityTokenGeneratorImpl securityTokenService
    ) {
        super();
        this.userService = userService;
        this.securityTokenService = securityTokenService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUserToDB(@RequestBody User user) throws UserAlreadyExistsException, MissingFieldsException {
        try {
            User createdUser = userService.saveUser(user);
            this.responseEntity = new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            LOG.error("Save failed for user: " + user.getUserName() + ". User already exists");
            throw new UserAlreadyExistsException(exceptionUserAlreadyExists);
        } catch (MissingFieldsException e) {
            LOG.error("Save failed. Missing fields");
            throw new MissingFieldsException(exceptionMissingData_msg);
        } catch (Exception e) {
            LOG.error("SGeneral Save Error. Message: " + e.getLocalizedMessage());
            this.responseEntity = new ResponseEntity<>(exceptionErrorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        LOG.info("User " + user.getUserName() + " added successfully.");
        return this.responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getUserToken(@RequestBody User user) throws UserNotFoundException, InvalidPasswordException {
        try {
            User checkUser = userService.validateLogin(user);
            responseEntity = new ResponseEntity<>(securityTokenService.generateToken(user), HttpStatus.OK);
        } catch (InvalidPasswordException e) {
            LOG.error("Login failed for user: " + user.getUserName() + ". Invalid Password.");
            throw new InvalidPasswordException(exceptionInvalidPassword);
        } catch (UserNotFoundException e) {
            LOG.error("Login failed for user: " + user.getUserName() + ". User Not Founbd");
            throw new UserNotFoundException(exceptionUserNotFound);
        }

        return this.responseEntity;
    }
}
