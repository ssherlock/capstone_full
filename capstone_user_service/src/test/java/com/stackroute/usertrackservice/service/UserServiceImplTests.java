package com.stackroute.usertrackservice.service;

import com.stackroute.usertrackservice.exception.TrackAlreadyExistsException;
import com.stackroute.usertrackservice.exception.UserAlreadyRegisteredException;
import com.stackroute.usertrackservice.exception.UserNotFoundException;
import com.stackroute.usertrackservice.model.Artist;
import com.stackroute.usertrackservice.model.Image;
import com.stackroute.usertrackservice.model.Track;
import com.stackroute.usertrackservice.model.User;
import com.stackroute.usertrackservice.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceImplTests {

    private User user;
    private Artist artist;
    private Image image[];
    private Track track;
    private List<Track> trackList;

    private Optional optional;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        image = new Image[1];
        image[0] = new Image("img123", "url for image", "200*300");
        artist = new Artist("artist123", "Artist123", "url for artist");
        track = new Track("track123", "new single", artist, "track1", "123", "song url", image);
        trackList = new ArrayList<Track>();
        trackList.add(track);

        user = new User("user1", "abc@gmail.com", trackList);

        optional = Optional.of(user);
    }


    @After
    public void tearDown() {

        image = null;
        artist = null;
        track = null;
        trackList = null;
        user = null;

    }


    @Test
    public void testRegisterUser() throws UserAlreadyRegisteredException {

        when(userRepository.insert(user)).thenReturn(user);

        User fetchUser = userServiceImpl.registerUser(user);

        Assert.assertEquals(user, fetchUser);

        verify(userRepository, times(1)).insert(user);

    }

//    @Test
//    public void testUpdateUser() throws UserNotFoundException, TrackAlreadyExistsException {
//
//        when(userRepository.save(user)).thenReturn(user);
//
//        User fetchUser = userServiceImpl.updateUser(user);
//
//        Assert.assertEquals(user, fetchUser);
//
//        verify(userRepository, times( 1)).insert(user);
//
//        verify(userRepository, times(1)).findById(user.getUserName());
//    }

   /* @Test
    public void testUpdateUserSuccess() throws UserNotFoundException {

        when(userRepository.save(user)).thenReturn(user);

        User fetchUser = userServiceImpl.updateUser(user);

        Assert.assertEquals(user, fetchUser);

        verify(userRepository, times(1)).save(user);

        verify(userRepository, times(1)).findById(user.getUserName());

    } */

}
