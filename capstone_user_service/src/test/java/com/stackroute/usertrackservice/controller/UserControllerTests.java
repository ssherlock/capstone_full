package com.stackroute.usertrackservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.usertrackservice.exception.TrackAlreadyExistsException;
import com.stackroute.usertrackservice.exception.TrackNotFoundException;
import com.stackroute.usertrackservice.exception.UserAlreadyRegisteredException;
import com.stackroute.usertrackservice.exception.UserNotFoundException;
import com.stackroute.usertrackservice.model.Artist;
import com.stackroute.usertrackservice.model.Image;
import com.stackroute.usertrackservice.model.Track;
import com.stackroute.usertrackservice.model.User;
import com.stackroute.usertrackservice.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    private User user;
    private User unknownUser;
    private Artist artist;
    private Image image[];
    private Track track, track2;
    private List<Track> trackList;
    private String USER_NAME = "user1";
    private String INVALID_USERNAME = "INVALID_USERNAME";
    private String UPDATED_COMMENT = "UPDATED_COMMENT";
    private String TRACK_ID = "TRACK_ID";

    private Optional optional;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() {

        image = new Image[1];
        image[0] = new Image("img123", "url for image", "200*300");
        artist = new Artist("artist123", "Artist123", "url for artist");
        track = new Track("track123", "new single", artist, "track1", "123", "song url", image);
        track2 = new Track("track456", "new single", artist, "track1", "100", "song url", image);
        trackList = new ArrayList<Track>();
        trackList.add(track);

        user = new User(USER_NAME, "abc@gmail.com", trackList);

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
    public void testRegisterUserSuccess() throws UserAlreadyRegisteredException, JsonProcessingException, Exception {

        when(userService.registerUser(user)).thenReturn(user);
        mockMvc.perform(post("/api/v1/usertracksservice/registerUser")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonToString(user)))
                .andExpect(status().isCreated())
                .andDo(print());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    public void testUserAlreadyregistered() throws UserAlreadyRegisteredException, JsonProcessingException, Exception {
        when(userService.registerUser(user)).thenThrow(UserAlreadyRegisteredException.class);
        mockMvc.perform(post("/api/v1/usertracksservice/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(user)))
                .andExpect(status().isConflict())
                .andDo(print());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    public void testAddTrackForUserSuccess() throws UserNotFoundException, JsonProcessingException, Exception {
        final User userTest = new User(USER_NAME,null,null);
        when(userService.updateUser(user, track2)).thenReturn(userTest);
        mockMvc.perform(post("/api/v1/usertracksservice/user/user1/track")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonToString(track2)))
                .andExpect(status().isCreated())
                .andDo(print());
        verify(userService, times(1)).updateUser(userTest, track2);
    }

    @Test
    public void testAddTrackForUserFailureTrackAlreadyExists() throws UserNotFoundException, JsonProcessingException, Exception {
        final User userTest = new User(USER_NAME,null,null);
        when(userService.updateUser(userTest, track)).thenThrow(TrackAlreadyExistsException.class);
        mockMvc.perform(post("/api/v1/usertracksservice/user/user1/track")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonToString(track)))
                .andExpect(status().isConflict())
                .andDo(print());
        verify(userService, times(1)).updateUser(userTest, track);
    }

    @Test
    public void testAddTrackForUserFailureUserNotFound() throws UserNotFoundException, JsonProcessingException, Exception {
        final User userTest = new User(USER_NAME,null,null);
        user.setUserName(INVALID_USERNAME);
        when(userService.updateUser(userTest, track)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(post("/api/v1/usertracksservice/user/user1/track")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonToString(track)))
                .andExpect(status().isNotFound())
                .andDo(print());
        //verify(userService, times(1)).updateUser(userTest, track);
    }

    @Test
    public void testUpdateTrackCommentSuccess() throws UserNotFoundException, TrackNotFoundException, TrackAlreadyExistsException, JsonProcessingException, Exception {
        track.setComments(UPDATED_COMMENT);
        when(userService.updateTrackComment(user, track)).thenReturn(user);
        mockMvc.perform(put("/api/v1/usertracksservice/user/user1/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(user)))
                .andExpect(status().isCreated())
                .andDo(print());
//        verify(userService, times(1)).updateTrackComment(user, track2);
    }

    @Test
    public void deleteTrackSuccess() throws Exception {
        when(userService.deleteTrack(USER_NAME, TRACK_ID)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/usertracksservice/user/{user}/{track}", USER_NAME, TRACK_ID)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
        verify(userService, times(1)).deleteTrack(USER_NAME, TRACK_ID);
    }

    @Test
    public void deleteTrack_UserNotFound() throws UserNotFoundException, Exception {
        doThrow(new TrackNotFoundException()).when(userService).deleteTrack(INVALID_USERNAME, "track123");
        mockMvc.perform(delete("/api/v1/usertracksservice/user/INVALID_USERNAME/track123")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(userService, times(1)).deleteTrack(INVALID_USERNAME, "track123");
    }

    @Test
    public void deleteTrack_NotFound() throws TrackNotFoundException, Exception {
        doThrow(new TrackNotFoundException()).when(userService).deleteTrack("user1", "track666");
        mockMvc.perform(delete("/api/v1/usertracksservice/user/user1/track666")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(userService, times(1)).deleteTrack("user1", "track666");
    }

    /**
     * Helper method for outputting JSON as readable text
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    private static String jsonToString(final Object object) throws JsonProcessingException {
        String result;

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(object);
            result = jsonContent;
        } catch(JsonProcessingException e) {
            result = "JSON processing error";
        }
        return result;
    }


}
