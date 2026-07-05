package com.himanshu.LinkUP.dto;

import com.himanshu.LinkUP.enums.ActivityType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityResponse {
    private Long id;
    private String userName;
    private String message;
    private ActivityType type;
    private LocalDateTime localDateTime;
}
