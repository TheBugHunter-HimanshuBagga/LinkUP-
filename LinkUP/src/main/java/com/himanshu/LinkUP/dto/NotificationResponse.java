package com.himanshu.LinkUP.dto;

import com.himanshu.LinkUP.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String message;
    private boolean isRead;
    private NotificationType type;
    private LocalDateTime createdAt;
}
