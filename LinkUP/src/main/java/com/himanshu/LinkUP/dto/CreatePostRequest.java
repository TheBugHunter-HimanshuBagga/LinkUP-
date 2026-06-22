package com.himanshu.LinkUP.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {
    @NotBlank
    private String content;
}
