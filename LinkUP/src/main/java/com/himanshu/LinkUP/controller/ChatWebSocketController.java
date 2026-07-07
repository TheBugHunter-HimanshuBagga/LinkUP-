package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.MessageResponse;
import com.himanshu.LinkUP.dto.SendMessageRequest;
import com.himanshu.LinkUP.entity.Message;
import com.himanshu.LinkUP.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate; // ity sends the message

    @MessageMapping("/chat") // websocket version of post mapping
    public void sendMessage(SendMessageRequest request, Principal principal ){
        Message savedMessage = messageService.sendWebSocketMessage(
                principal.getName(),
                request
        );
        MessageResponse response = MessageResponse.builder()
                .id(savedMessage.getId())
                .senderName(savedMessage.getSender().getFullName())
                .receiverName(savedMessage.getReceiver().getFullName())
                .content(savedMessage.getContent())
                .sentAt(savedMessage.getSentAt())
                .build();

        simpMessagingTemplate.convertAndSendToUser(
                savedMessage.getReceiver().getEmail(),
                "/queue/messages",
                response
        );
    }

}
