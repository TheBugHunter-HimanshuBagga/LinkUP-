package com.himanshu.LinkUP.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostResponse implements Serializable {
    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
}
