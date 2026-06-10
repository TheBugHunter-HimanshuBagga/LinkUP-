package com.himanshu.LinkUP.dto;

import com.himanshu.LinkUP.enums.ConnectionStatus;
import com.himanshu.LinkUP.service.ConnectionService;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentRequestResponse {
    private Long receiverId;
    private String receiverName;
    private ConnectionStatus status;
}
