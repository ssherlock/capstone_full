package com.stackroute.authenticationservice.service;


import com.stackroute.authenticationservice.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ISecurityTokenGenerator {
    public Map<String, String> generateToken(final User user);
}

