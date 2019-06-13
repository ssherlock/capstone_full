package com.stackroute.authenticationservice.service;

import com.stackroute.authenticationservice.exception.MissingFieldsException;
import com.stackroute.authenticationservice.exception.UserAlreadyExistsException;
import com.stackroute.authenticationservice.model.User;
import com.stackroute.authenticationservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private Optional optional;
    private User user;
    private LocalDate localDate;
    private Date datePlus1Day;
    private String jwtToken;
    private SecurityTokenGeneratorImpl securityTokenGenerator;

    @InjectMocks
    private UserServiceImpl userService;

    private static final boolean TRUE = true;
    private static final boolean FALSE = false;
    private static final String NAME = "NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String SHARED_KEY = "SHARED_KEY";
    private final String INVALID_USER = "INVALID_USER";

    private User returnedUser;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setUserName(NAME);
        user.setPassword(PASSWORD);
        optional = Optional.of(user);
        localDate = LocalDate.now();
        //Gets todays date plus one day (to be used as expiration date)
//        datePlus1Day = java.sql.Date.valueOf(localDate.plusDays(1));
//        jwtToken = Jwts.builder().setSubject(user.getUserName()).setIssuedAt(java.sql.Date.valueOf(localDate))
//                .signWith(SignatureAlgorithm.HS512,SHARED_KEY).setExpiration(datePlus1Day).compact();
        jwtToken = Jwts.builder().setSubject(user.getUserName()).setIssuedAt(java.sql.Date.valueOf(localDate))
                .signWith(SignatureAlgorithm.HS512,SHARED_KEY).compact();
    }

    @After
    public void tearDown()
    {
        user = null;
        userRepository = null;
        userService = null;
        optional = null;
        localDate = null;
        datePlus1Day = null;
        jwtToken = null;
    }


    @Test
    public void testSaveUser() throws UserAlreadyExistsException, MissingFieldsException {

        when(userRepository.existsById(NAME)).thenReturn(FALSE);
        when(userRepository.save(user)).thenReturn(user);
        returnedUser = userService.saveUser(user);
        Assert.assertThat(returnedUser, is(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testUserThrowsAlreadyExistsException() throws MissingFieldsException, UserAlreadyExistsException {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(user.getUserName())).thenReturn(optional);
        User fetchUser = userService.saveUser(user);
        Assert.assertEquals(user, fetchUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test(expected = MissingFieldsException.class)
    public void testUserThrowsMissingFieldsException() throws MissingFieldsException, UserAlreadyExistsException {
        user = new User();
        user.setUserName(NAME);
        //password not set so should throw MissingFieldsException
        when(userRepository.existsById(NAME)).thenReturn(TRUE);
        returnedUser = userService.saveUser(user);
        verify(userService, times(1)).saveUser(user);
    }

    //Test the token generation
    //Expiration date tests removed as not used during testing
//    @Test
//    public void testGenerateTokenExpirationDateOK() throws MissingFieldsException, UserAlreadyExistsException {
//        when(userService.saveUser(user)).thenReturn(user);
//
//        Date expDate = Jwts.parser().setSigningKey(SHARED_KEY).parseClaimsJws(jwtToken).getBody().getExpiration();
//        Assert.assertEquals(expDate, datePlus1Day);
//    }
//
//    @Test
//    public void testGenerateTokenExpirationDateExpired() throws MissingFieldsException, UserAlreadyExistsException {
//        when(userService.saveUser(user)).thenReturn(user);
//
//        Date expDate = Jwts.parser().setSigningKey(SHARED_KEY).parseClaimsJws(jwtToken).getBody().getExpiration();
//        Assert.assertNotEquals(expDate, java.sql.Date.valueOf(localDate));
//    }

    @Test
    public void testGenerateTokenCheckBody() throws MissingFieldsException, UserAlreadyExistsException {
        when(userService.saveUser(user)).thenReturn(user);

        String userFromBody = Jwts.parser().setSigningKey(SHARED_KEY).parseClaimsJws(jwtToken).getBody().getSubject();
        Assert.assertEquals(userFromBody, user.getUserName());
    }

    @Test
    public void testGenerateTokenCheckBodyInvalid() throws MissingFieldsException, UserAlreadyExistsException {
        when(userService.saveUser(user)).thenReturn(user);

        String userFromBody = Jwts.parser().setSigningKey(SHARED_KEY).parseClaimsJws(jwtToken).getBody().getSubject();
        Assert.assertNotEquals(userFromBody, INVALID_USER);
    }
}
