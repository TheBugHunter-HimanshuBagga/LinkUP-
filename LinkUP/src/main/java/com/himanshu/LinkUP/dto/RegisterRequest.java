package com.himanshu.LinkUP.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotNull
    private String fullName;

    @Email
    @NotNull
    private String email;

    @Size(min = 6)
    private String password;

    @Min(18)
    @Max(40)
    private Integer age;

    @NotNull
    private String gender;

    @NotNull
    private String city;

    @NotNull
    private String college;

    @NotNull
    private String branch;

    @NotNull
    private Integer year;

    @NotBlank
    private String bio;

    @NotBlank
    private String skills;

    @NotBlank
    private String interests;

}
