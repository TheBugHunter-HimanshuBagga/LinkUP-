package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.PendingRequestResponse;
import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ConnectionStatus;
import com.himanshu.LinkUP.repository.ConnectionRequestRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ConnectionRequestService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionRequestServiceImpl implements ConnectionRequestService {
    private final UserRepository userRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    private final ModelMapper modelMapper;
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


    @Override
    public List<PendingRequestResponse> pendingRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User receiver = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Receiver Not Found")
        );
        List<ConnectionRequest> requests = connectionRequestRepository.findByReceiverAndStatus(receiver , ConnectionStatus.PENDING);
        return requests.stream()
                .map(request ->
                        PendingRequestResponse.builder()
                        .requestId(request.getId())
                        .senderName(request.getSender().getFullName())
                        .build())
                .toList();
    }


    @Override
    public String acceptPendingRequest(Long receiverId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found")
                        );
        ConnectionRequest request = connectionRequestRepository.findById(receiverId)
                .orElseThrow(() ->
                        new RuntimeException("Request Not Found")
                        );
        if(!request.getReceiver().getId().equals(currentUser.getId())){
            throw new RuntimeException(
                    "You are not authorized to accept the request"
            );
        }
        if(request.getStatus() != ConnectionStatus.PENDING){
            throw new RuntimeException(
                    "Request is already processed"
            );
        }
        request.setStatus(ConnectionStatus.ACCEPTED);
        connectionRequestRepository.save(request);
        return "Request Accepted Successfully";
    }
}
