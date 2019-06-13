package com.stackroute.usertrackservice.repository;

import com.stackroute.usertrackservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
