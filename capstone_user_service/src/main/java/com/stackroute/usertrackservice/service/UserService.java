package com.stackroute.usertrackservice.service;

import com.stackroute.usertrackservice.exception.TrackAlreadyExistsException;
import com.stackroute.usertrackservice.exception.TrackNotFoundException;
import com.stackroute.usertrackservice.exception.UserAlreadyRegisteredException;
import com.stackroute.usertrackservice.exception.UserNotFoundException;
import com.stackroute.usertrackservice.model.Track;
import com.stackroute.usertrackservice.model.User;

import java.util.List;

public interface UserService {

    public User registerUser(User user) throws UserAlreadyRegisteredException;
    public List getAllTracks(User user) throws UserNotFoundException;
    public User updateUser(User user, Track track) throws UserNotFoundException, TrackAlreadyExistsException;
    public User updateTrackComment(User user, Track track) throws UserNotFoundException, TrackNotFoundException;
    public boolean deleteTrack(String userName, String trackId) throws UserNotFoundException, TrackNotFoundException;

}
