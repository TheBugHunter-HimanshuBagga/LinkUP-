package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.PendingRequestResponse;
import com.himanshu.LinkUP.service.ConnectionRequestService;
import com.himanshu.LinkUP.service.impl.ConnectionRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connections")
public class ConnectionController {
    private final ConnectionRequestService connectionRequestService;
    @PostMapping("/send/{receiverId}")
    public ResponseEntity<String> sendRequest(@PathVariable Long receiverId){
        connectionRequestService.sendRequest(receiverId);
        return ResponseEntity.ok("Connection Request Sent©");
    }
    @GetMapping("/pending")
    public List<PendingRequestResponse> getPendingRequests(){
        List<PendingRequestResponse> pendingRequestResponse = connectionRequestService.pendingRequest();
        return pendingRequestResponse;
    }
    @PutMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptPendingRequests(@PathVariable Long requestId){
        connectionRequestService.acceptPendingRequest(requestId);
        return ResponseEntity.ok("Connection request has been accepted");
    }

}
