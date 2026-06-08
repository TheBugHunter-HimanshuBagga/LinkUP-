package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.PendingRequestResponse;

import java.util.List;

public interface ConnectionRequestService {
    void sendRequest(Long receiverId);
    List<PendingRequestResponse> pendingRequest();
    String acceptPendingRequest(Long requestId);
    String rejectPendingRequest(Long receiverId);
}
