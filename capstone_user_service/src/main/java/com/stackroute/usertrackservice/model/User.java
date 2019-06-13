package com.stackroute.usertrackservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document
public class User {

    @Id
    private String userName;
    private String emailAddress;
    private List<Track> trackList;
}
