package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.MessageResponse;
import com.himanshu.LinkUP.dto.SendMessageRequest;
import com.himanshu.LinkUP.entity.Message;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.repository.MessageRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    @Override
    public Message sendMessage(SendMessageRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Logged-in user
        User sender = userRepository.findByEmail(email)
                .orElseThrow(
                () -> new RuntimeException("Sender doesn't exists")
        );

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(
                        () -> new RuntimeException("Receiver not found")
                );

        // can't send message to Yourself
        if(sender.getId().equals(receiver.getId())){
            throw new RuntimeException(
                    "You cannot send message to yourself"
            );
        }

        Message message = Message.builder()
                .content(request.getContent())
                .sender(sender) // logged-in user
                .receiver(receiver) // person who will receive the message
                .sentAt(LocalDateTime.now())
                .build();



        // WEBSOCKET LATER

        return messageRepository.save(message);
    }

    @Override
    public List<MessageResponse> getConversation(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email).
                orElseThrow(
                        () -> new RuntimeException("Current user not found")
                );

        // check if other user exists
        userRepository.findById(userId).
                orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        // if every thing is ok

        return messageRepository.findConversation(currentUser.getId(),userId)
                .stream()
                .map(
                        message -> MessageResponse.builder()
                                .id(message.getId())
                                .senderName(message.getSender().getFullName())
                                .receiverName(message.getReceiver().getFullName())
                                .content(message.getContent())
                                .sentAt(message.getSentAt())
                                .build()
                )
                .toList();

    }

    @Override
    public Message sendWebSocketMessage(String email, SendMessageRequest request) {
        // Logged-in user (obtained from Principal)
        User sender = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("Sender doesn't exist")
                );

        // Receiver
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(
                        () -> new RuntimeException("Receiver not found")
                );

        // Cannot send message to yourself
        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException(
                    "You cannot send message to yourself"
            );
        }

        Message message = Message.builder()
                .content(request.getContent())
                .sender(sender)
                .receiver(receiver)
                .sentAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }
}
