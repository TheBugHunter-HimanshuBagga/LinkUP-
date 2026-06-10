package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.MyConnectionResponse;
import com.himanshu.LinkUP.dto.SentRequestResponse;
import com.himanshu.LinkUP.entity.Connection;
import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ConnectionStatus;
import com.himanshu.LinkUP.repository.ConnectionRepository;
import com.himanshu.LinkUP.repository.ConnectionRequestRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    @Override
    public List<MyConnectionResponse> myConnections() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User Not Found")
        );
        List<Connection> connections = connectionRepository.findByUser1OrUser2(currentUser , currentUser);

        return connections.stream()
                .map(connection -> {
                    User otherUser;
                    if(connection.getUser1().getId().equals(currentUser.getId())){
                        otherUser = connection.getUser2();
                    }
                    else{
                        otherUser = connection.getUser1();
                    }
                    return MyConnectionResponse.builder()
                            .userId(otherUser.getId())
                            .fullName(otherUser.getFullName())
                            .build();
                })
                .toList();
    }

    @Override
    public String withdrawRequest(Long requestId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not Found!!")
        );
        ConnectionRequest request = connectionRequestRepository.findById(requestId).orElseThrow(
                () -> new RuntimeException("Request Not Found")
        );
        if(!request.getSender().getId().equals(currentUser.getId())){
            throw new RuntimeException(
                    "You are not authorized to withdraw the request"
            );
        }
        if(request.getStatus() != ConnectionStatus.PENDING){
            throw new RuntimeException(
                    "Only Pending request can be withdrawn, Your request was already processed"
            );
        }
        connectionRequestRepository.delete(request);
        return "Your request WITHDRAWN successfully";
    }

    @Override
    public List<SentRequestResponse> sentRequests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Current User Not Found")
        );
        // to see the sent Request i need to check it from the ConnectionRequest
        List<ConnectionRequest> requests = connectionRequestRepository.findBySender(currentUser);
        return requests.stream()
                .map(request ->
                        SentRequestResponse.builder()
                                .receiverId(request.getReceiver().getId())
                                .receiverName(request.getReceiver().getFullName())
                                .status(request.getStatus())
                                .build())
                .toList();
    }
}
