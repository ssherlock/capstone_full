package com.stackroute.usertrackservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Track {

    @JsonProperty("id")
    private String trackId;
    private String comments;
    private Artist artist;
    private String name;
    private String listeners;
    private String url;
    @JsonProperty("images")
    private Image image[];

}