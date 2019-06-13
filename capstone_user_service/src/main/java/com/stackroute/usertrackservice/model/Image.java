package com.stackroute.usertrackservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Image {

    private String imageId;
    private String url;
    private String size;

}
