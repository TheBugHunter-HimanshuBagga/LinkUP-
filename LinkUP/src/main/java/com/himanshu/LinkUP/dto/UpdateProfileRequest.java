package com.himanshu.LinkUP.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest { // what user can update
    private String bio;
    private String skills;
    private String interests;
    private String city;
}
