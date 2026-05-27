package com.himanshu.LinkUP.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;

    private String fullName;

    private Integer age;

    private String city;

    private String college;

    private String branch;

    private String skills;

    private String interests;
}
