package com.stackroute.orchestrationservice.service;

import com.stackroute.orchestrationservice.domain.User;
import com.stackroute.orchestrationservice.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrchestrationServiceImpl implements OrchestrationService {


    @Autowired
    RestTemplate restTemplate;

    String urlUserTrackService = "http://musicuserservice/api/v1/usertracksservice/registerUser";
    String urlAuthenticationService = "http://musicauthenticationservice/api/v1/authservice/save";


    public User registerUser(User user) throws UserAlreadyExistsException {

        User userResponse = null;

        try {

            userResponse = restTemplate.postForObject(urlUserTrackService, user, User.class);

            restTemplate.postForObject(urlAuthenticationService, user, User.class);

        } catch(Exception e) {

            throw new UserAlreadyExistsException();
        }

        return userResponse;
    }

}
