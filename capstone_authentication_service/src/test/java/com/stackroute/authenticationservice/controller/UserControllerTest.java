package com.stackroute.authenticationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.authenticationservice.exception.InvalidPasswordException;
import com.stackroute.authenticationservice.exception.MissingFieldsException;
import com.stackroute.authenticationservice.exception.UserAlreadyExistsException;
import com.stackroute.authenticationservice.exception.UserNotFoundException;
import com.stackroute.authenticationservice.model.User;
import com.stackroute.authenticationservice.repository.UserRepository;
import com.stackroute.authenticationservice.service.SecurityTokenGeneratorImpl;
import com.stackroute.authenticationservice.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private User invalidUserNameUser;
    private User invalidPasswordUser;

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private SecurityTokenGeneratorImpl securityTokenGenerator;

    private static final boolean TRUE = true;
    private static final boolean FALSE = false;
    private static final String NAME = "NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String SHARED_KEY = "SHARED_KEY";
    private final String INVALID_USER = "INVALID_USER";
    private final String INVALID_PASSWORD = "INVALID_PASSWORD";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setUserName(NAME);
        user.setPassword(PASSWORD);

        invalidUserNameUser = new User();
        invalidUserNameUser.setUserName(INVALID_USER);
        invalidUserNameUser.setPassword(PASSWORD);

        invalidPasswordUser = new User();
        invalidPasswordUser.setUserName(NAME);
        invalidPasswordUser.setPassword(INVALID_PASSWORD);
    }

    @Test
    public void testSaveUser() throws Exception {
        when(this.userService.saveUser(user)).thenReturn(user);
        mockMvc.perform(post("/api/v1/authservice/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(jsonToString(user)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(PASSWORD))
                .andDo(print());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testSaveUserThrowsExistsException() throws Exception {
        when(userService.saveUser(user)).thenThrow(UserAlreadyExistsException.class);
        mockMvc.perform(post("/api/v1/authservice/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(jsonToString(user)))
                .andDo(print());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testValidateLogin() throws Exception {
        when(userService.saveUser(user)).thenReturn(user);
        when(userService.validateLogin(user)).thenReturn(user);
        mockMvc.perform(post("/api/v1/authservice/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(jsonToString(user)))
                .andDo(print());
        verify(userService, times(1)).validateLogin(user);
    }

    @Test
    public void testValidateLoginUserUnknown() throws Exception {
        when(userService.saveUser(user)).thenReturn(user);
        when(userService.validateLogin(invalidUserNameUser)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(post("/api/v1/authservice/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(jsonToString(invalidUserNameUser)))
                .andDo(print());
    }

    @Test
    public void testValidateLoginPasswordIncorrect() throws Exception {
        when(userService.saveUser(user)).thenReturn(user);
        when(userService.validateLogin(invalidPasswordUser)).thenThrow(InvalidPasswordException.class);
        mockMvc.perform(post("/api/v1/authservice/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8).
                        content(jsonToString(invalidUserNameUser)))
                .andDo(print());
    }


    private static String jsonToString(final Object ob) throws JsonProcessingException {
        String result;
        try {
            result = new ObjectMapper().writeValueAsString(ob);
        } catch(JsonProcessingException e) {
            result = "JSON processing error";
        }
        return result;
    }


}
