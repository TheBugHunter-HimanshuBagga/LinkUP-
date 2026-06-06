package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ConnectionStatus;
import com.himanshu.LinkUP.repository.ConnectionRequestRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ConnectionRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConnectionRequestServiceImpl implements ConnectionRequestService {
    private final UserRepository userRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    @Override
    public void sendRequest(Long receiverId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User sender = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User Not Exists")
        );
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() ->
                        new RuntimeException("The Person you are trying to send the request does not Exists")
                        );
        // what if the user send request to himself
        if(sender.getId().equals(receiver.getId())){
            throw new RuntimeException(
                    "You can't send request to yourself"
            );
        }
        if(connectionRequestRepository.existsBySenderAndReceiver(sender , receiver)){
            throw new RuntimeException(
                    "Request already sent"
            );
        }
        ConnectionRequest request = ConnectionRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(ConnectionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        connectionRequestRepository.save(request);
    }
}
