package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.MyConnectionResponse;
import com.himanshu.LinkUP.dto.SentRequestResponse;

import java.util.List;

public interface ConnectionService {
    List<MyConnectionResponse> myConnections();
    String withdrawRequest(Long requestId);
    List<SentRequestResponse> sentRequests();
    Long connectionCount();
    String removeConnectionById(Long connectionId);
}
