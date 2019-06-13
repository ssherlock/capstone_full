package com.stackroute.usertrackservice.controller;

import com.stackroute.usertrackservice.exception.TrackAlreadyExistsException;
import com.stackroute.usertrackservice.exception.TrackNotFoundException;
import com.stackroute.usertrackservice.exception.UserAlreadyRegisteredException;
import com.stackroute.usertrackservice.exception.UserNotFoundException;
import com.stackroute.usertrackservice.model.Track;
import com.stackroute.usertrackservice.model.User;
import com.stackroute.usertrackservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/usertracksservice")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private ResponseEntity responseEntity;

    @Value("${exception.error_msg}")
    private String exceptionErrorMsg;
    @Value("${exception.userNotFound_msg}")
    private String exceptionUserNotFound;
    @Value("${exception.invalidPassword_msg}")
    private String exceptionInvalidPassword;
    @Value("${exception.userAlreadyRegistered_msg}")
    private String exceptionUserAlreadyRegistered;
    @Value("${exception.missingData_msg}")
    private String exceptionMissingData_msg;
    @Value("${exception.trackAlreadyExists_msg}")
    private String exceptionTrackAlreadyExists_msg;
    @Value("${exception.trackNotFound_msg}")
    private String excepionTrackNotFound_msg;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;

    }


    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyRegisteredException {

        try {
            User registeredUser = userService.registerUser(user);
            responseEntity = new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }
        catch(UserAlreadyRegisteredException e) {
            LOG.error("Registration Failed For: " + user.getUserName() + ". User Already Registered");
            throw new UserAlreadyRegisteredException(exceptionUserAlreadyRegistered);
        }
        catch(Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        LOG.info(user.getUserName() + " Successfully Registered");
        return responseEntity;
    }

    //Get all Track details of the User - John
    //With this request token should come from frontend in the request
    //else exception will come Missing or invalid token
    @GetMapping("user/{username}/tracks")
    public ResponseEntity<?> getAllTracksFromDB(@PathVariable("username") String username) throws UserNotFoundException{
        List<User> trackList = null;
        try {
            User user = new User(username, null, null);
            trackList = this.userService.getAllTracks(user);
            this.responseEntity = new ResponseEntity<>(trackList, HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            throw new UserNotFoundException(exceptionUserNotFound);
        }
        catch (Exception e) {
            LOG.error("getAllTracksFromDB Failed with: " + e.getLocalizedMessage());
            this.responseEntity = new ResponseEntity<>(exceptionErrorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }



    //Delete the Track for the User John
    //With this request token should come from frontend in the request
    //else exception will come Missing or invalid token
    @DeleteMapping("user/{userName}/{trackId}")
    public ResponseEntity<?> deleteTrackFromDb(@PathVariable("userName") String userName, @PathVariable("trackId") String trackId) throws UserNotFoundException, TrackNotFoundException {

        try {

            if (userService.deleteTrack(userName, trackId)) {

                responseEntity = new ResponseEntity<>("Track Id : " + trackId + " deleted for user " + userName, HttpStatus.OK);

            } else {

                throw new TrackNotFoundException();
            }

        } catch (UserNotFoundException e) {

            throw new UserNotFoundException(exceptionUserNotFound);

        } catch (TrackNotFoundException e) {

            throw new TrackNotFoundException(excepionTrackNotFound_msg);

        } catch (Exception e) {

            this.responseEntity = new ResponseEntity(exceptionErrorMsg, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return responseEntity;
    }



    //Add a track to the database.
    //With this request token should come from frontend in the request
    //else exception will come Missing or invalid token
    @PostMapping("user/{username}/track")
    public ResponseEntity<?> addTrackForUser(@PathVariable("username") String username, @RequestBody Track track) throws UserNotFoundException, TrackAlreadyExistsException {
        try {
            User user = new User(username, null, null);
            User updatedUser = userService.updateUser(user, track);
            LOG.info("Track added for user:" + user.getUserName() +" successfully");
            System.out.println(" track data" + updatedUser);
            this.responseEntity = new ResponseEntity<>(updatedUser, HttpStatus.CREATED);

        } catch (UserNotFoundException e) {
            System.out.println("UserNotFoundException: " + e.getLocalizedMessage());
            throw new UserNotFoundException(exceptionUserNotFound);
        }
        catch (TrackAlreadyExistsException e) {
            System.out.println("TrackAlreadyExistsException: " + e.getLocalizedMessage());
            throw new TrackAlreadyExistsException(exceptionTrackAlreadyExists_msg);
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
            this.responseEntity = new ResponseEntity<>(exceptionErrorMsg , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseEntity;
    }


    //Update track for User John
    //With this request token should come from frontend in the request
    //else exception will come Missing or invalid token
    @PutMapping("user/{username}/track")
    public ResponseEntity<?> updateTrackComment(@PathVariable("username") String username, @RequestBody Track track) throws UserNotFoundException, TrackAlreadyExistsException {
        try {
            User user = new User(username, null, null);
            User updatedUser = userService.updateTrackComment(user, track);
            LOG.info("Track updated for user:" + user.getUserName() +" successfully");
            this.responseEntity = new ResponseEntity<>(updatedUser, HttpStatus.CREATED);

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(exceptionUserNotFound);
        }
        catch (TrackNotFoundException e) {
            throw new TrackAlreadyExistsException(exceptionTrackAlreadyExists_msg);
        }
        catch(Exception e) {
            this.responseEntity = new ResponseEntity<>(exceptionErrorMsg , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseEntity;
    }

}

