package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.MyConnectionResponse;
import com.himanshu.LinkUP.dto.PendingRequestResponse;
import com.himanshu.LinkUP.dto.SentRequestResponse;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.service.ConnectionRequestService;
import com.himanshu.LinkUP.service.ConnectionService;
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
    private final ConnectionService connectionService;
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
    @PutMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectPendingRequest(@PathVariable Long requestId){
        connectionRequestService.rejectPendingRequest(requestId);
        return ResponseEntity.ok("Connection request has been declined™");
    }
    @GetMapping("/my-connections")
    public List<MyConnectionResponse> getMyConnections(){
        List<MyConnectionResponse> myConnectionResponses = connectionService.myConnections();
        return myConnectionResponses;
    }
    @DeleteMapping("/withdraw/{requestId}")
    public ResponseEntity<String> withdrawRequest(@PathVariable Long requestId){
        String message = connectionService.withdrawRequest(requestId);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/sent")
    public List<SentRequestResponse> sentByMe(){ //Requests sent BY me
        List<SentRequestResponse> sentRequestResponse = connectionService.sentRequests();
        return sentRequestResponse;
    }
}
