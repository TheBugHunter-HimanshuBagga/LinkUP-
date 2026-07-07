package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.MessageResponse;
import com.himanshu.LinkUP.dto.SendMessageRequest;
import com.himanshu.LinkUP.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    // send a message
    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageRequest request){
        messageService.sendMessage(request);
        return ResponseEntity.ok("Message sent successfully");
    }

    // get conversation with another user
    @GetMapping("/{userId}")
    public ResponseEntity<List<MessageResponse>> getConversation(@PathVariable Long userId){
        List<MessageResponse> conversation = messageService.getConversation(userId);
        return ResponseEntity.ok(conversation);
    }
}
