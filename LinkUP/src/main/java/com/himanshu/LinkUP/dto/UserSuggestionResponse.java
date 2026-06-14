package com.himanshu.LinkUP.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSuggestionResponse {
    private Long id;
    private String fullName;
}
