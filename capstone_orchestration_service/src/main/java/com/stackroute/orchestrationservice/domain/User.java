package com.stackroute.orchestrationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private String userName;
    private String emailAddress;
    private String password;
}
