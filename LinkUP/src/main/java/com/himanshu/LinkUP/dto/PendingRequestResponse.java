package com.himanshu.LinkUP.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingRequestResponse {
    private Long requestId;
    private String senderName;
}
