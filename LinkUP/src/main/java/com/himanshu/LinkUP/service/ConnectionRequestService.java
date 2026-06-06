package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.PendingRequestResponse;
import com.himanshu.LinkUP.entity.User;

import java.util.List;

public interface ConnectionRequestService {
    void sendRequest(Long receiverId);
    List<PendingRequestResponse> pendingRequest();
}
