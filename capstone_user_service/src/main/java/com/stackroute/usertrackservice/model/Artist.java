package com.stackroute.usertrackservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Artist {

    @JsonProperty("id")
    private String artistId;
    private String name;
    private String url;

}