package com.stackroute.orchestrationservice.controller;


import com.stackroute.orchestrationservice.domain.User;
import com.stackroute.orchestrationservice.exception.UserAlreadyExistsException;
import com.stackroute.orchestrationservice.service.OrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OrchestrationController {

    OrchestrationService orchestrationService;

    @Autowired
    OrchestrationController(OrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerAndSave(@RequestBody User user) throws UserAlreadyExistsException {
        ResponseEntity responseEntity = null;

        try {

            User userObj = this.orchestrationService.registerUser(user);
            responseEntity = new ResponseEntity(userObj, HttpStatus.CREATED);

        } catch(UserAlreadyExistsException e) {

            responseEntity = new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
        return responseEntity;
    }




}
