package com.stackroute.usertrackservice.service;

import com.stackroute.usertrackservice.exception.TrackAlreadyExistsException;
import com.stackroute.usertrackservice.exception.TrackNotFoundException;
import com.stackroute.usertrackservice.exception.UserAlreadyRegisteredException;
import com.stackroute.usertrackservice.exception.UserNotFoundException;
import com.stackroute.usertrackservice.model.Track;
import com.stackroute.usertrackservice.model.User;
import com.stackroute.usertrackservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    @Override
    public User registerUser(User user) throws UserAlreadyRegisteredException {

        Optional optional = userRepository.findById(user.getUserName());

        if(optional.isPresent()) {

            throw new UserAlreadyRegisteredException();
        }

        User createdUser = userRepository.insert(user);

        return createdUser;
    }

    @Override
    public User updateUser(User user, Track newTrack) throws UserNotFoundException, TrackAlreadyExistsException {
        User currentUser = null;
        List<Track> newTrackList = null;
        Optional<User> optional = userRepository.findById(user.getUserName());
        if(optional.isPresent()) {
            currentUser = optional.get();

            if(currentUser.getTrackList()!=null && !currentUser.getTrackList().isEmpty()) {

                //Check track doesn't exist before saving it
                for (Track track : currentUser.getTrackList()) {
                    if (track.getTrackId().equals(newTrack.getTrackId())) {
                        throw new TrackAlreadyExistsException();
                    }
                }

                //Ok to save track as checked against current tracks for user
                //Add new track to current track list
                newTrackList = currentUser.getTrackList();

            } else {

                newTrackList = new ArrayList<Track>();

            }

            newTrackList.add(newTrack);
            currentUser.setTrackList(newTrackList);
            currentUser = userRepository.save(currentUser);

        }
        else {
            System.out.println("IN the service " + currentUser);
            throw new UserNotFoundException();
        }
        return currentUser;
    }

    @Override
    public User updateTrackComment(User user, Track newTrack) throws UserNotFoundException, TrackNotFoundException {
        User currentUser = null;
        boolean trackFound = false;

        Optional<User> optional = userRepository.findById(user.getUserName());
        if(optional.isPresent()) {
            currentUser = optional.get();

            //Check track doesn't exist before saving it
            for (Track track : currentUser.getTrackList()) {
                if (track.getTrackId().equals(newTrack.getTrackId())) {
                    trackFound = true;
                    track.setComments(newTrack.getComments());
                    break;
                }
            }

            if (trackFound) {
                //Add new track to current track list
                List<Track> newTrackList = currentUser.getTrackList();
                currentUser.setTrackList(newTrackList);
                currentUser = userRepository.save(currentUser);
            }
            else {
                throw new TrackNotFoundException();
            }
        }
        else {
            throw new UserNotFoundException();
        }
        return currentUser;
    }

    @Override
    public List getAllTracks(User user) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(user.getUserName());
        User foundUser = null;

        if(optional.isPresent()) {
            foundUser = optional.get();
        }
        else {
            throw new UserNotFoundException();
        }

        List<Track> tracksList = foundUser.getTrackList();

        return tracksList;
    }


    @Override
    public boolean deleteTrack(String userName, String trackId) throws UserNotFoundException, TrackNotFoundException {

        boolean deleted = false;

        Optional<User> optional = userRepository.findById(userName);

        User foundUser = null;

        List<Track> trackList = null;

        if (optional.isPresent()) {

            foundUser = optional.get();

            trackList = foundUser.getTrackList();

            if (trackList!=null||!trackList.isEmpty()) {

                for(int i =0; i < trackList.size(); i++) {

                    if (trackList.get(i).getTrackId().equals(trackId)) {

                        trackList.remove(i);

                        foundUser.setTrackList(trackList);

                        userRepository.save(foundUser);

                        deleted = true;

                        break;
                    }
                }

            } else {

                throw new TrackNotFoundException();
            }

        } else {

            throw new UserNotFoundException();
        }

        return deleted;
    }
}
