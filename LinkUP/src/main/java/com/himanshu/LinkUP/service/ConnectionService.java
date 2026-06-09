package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.MyConnectionResponse;

import java.util.List;

public interface ConnectionService {
    List<MyConnectionResponse> myConnections();
    String withdrawRequest(Long requestId);
}
