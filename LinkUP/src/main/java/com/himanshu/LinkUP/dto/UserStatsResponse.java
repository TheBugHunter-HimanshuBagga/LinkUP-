package com.himanshu.LinkUP.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserStatsResponse {
    Long connectionCount;
    Long pendingRequestCount;
    Long sentRequestCount;
}
