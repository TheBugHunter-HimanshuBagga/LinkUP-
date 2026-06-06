package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.service.ConnectionRequestService;
import com.himanshu.LinkUP.service.impl.ConnectionRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}
