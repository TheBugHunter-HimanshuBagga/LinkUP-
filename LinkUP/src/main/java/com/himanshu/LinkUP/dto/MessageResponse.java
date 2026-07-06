package com.himanshu.LinkUP.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private String senderName;
    private String receiverName;
    private String content;
    private LocalDateTime sentAt;
}
