package com.stackroute.authenticationservice.service;

import com.stackroute.authenticationservice.exception.InvalidPasswordException;
import com.stackroute.authenticationservice.exception.MissingFieldsException;
import com.stackroute.authenticationservice.exception.UserAlreadyExistsException;
import com.stackroute.authenticationservice.exception.UserNotFoundException;
import com.stackroute.authenticationservice.model.User;
import com.stackroute.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) throws UserAlreadyExistsException, MissingFieldsException {
        final Optional<User> returnedOptionalUser = userRepository.findById(user.getUserName());
        if (returnedOptionalUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        if (null == user.getUserName() || user.getUserName().isEmpty() ||
                null == user.getPassword() || user.getPassword().isEmpty()) {
            throw new MissingFieldsException();
        }
        return this.userRepository.save(user);
    }

    @Override
    public User validateLogin(final User user) throws UserNotFoundException, InvalidPasswordException {
        final Optional<User> returnedOptionalUser = userRepository.findById(user.getUserName());
        if (returnedOptionalUser.isPresent()) {
            //Check password correct
            if (!user.getPassword().equals(returnedOptionalUser.get().getPassword())) {
                //Now check password
                throw new InvalidPasswordException();
            }
        } else {
            throw new UserNotFoundException();
        }
        return returnedOptionalUser.get();
    }
}
