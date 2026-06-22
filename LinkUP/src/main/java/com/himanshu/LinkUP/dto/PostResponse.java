package com.himanshu.LinkUP.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
}
