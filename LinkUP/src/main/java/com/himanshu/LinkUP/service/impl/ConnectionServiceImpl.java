package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.MyConnectionResponse;
import com.himanshu.LinkUP.dto.SentRequestResponse;
import com.himanshu.LinkUP.dto.UserSuggestionResponse;
import com.himanshu.LinkUP.entity.Connection;
import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ConnectionStatus;
import com.himanshu.LinkUP.exception.BadRequestException;
import com.himanshu.LinkUP.exception.ForbiddenException;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.repository.ConnectionRepository;
import com.himanshu.LinkUP.repository.ConnectionRequestRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                () -> new ResourceNotFoundException("User not found")
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
                () -> new ResourceNotFoundException("User not found")
        );
        ConnectionRequest request = connectionRequestRepository.findById(requestId).orElseThrow(
                () -> new ResourceNotFoundException("Connection request not found")
        );
        if(!request.getSender().getId().equals(currentUser.getId())){
            throw new ForbiddenException(
                    "You are not authorized to withdraw this request"
            );
        }
        if(request.getStatus() != ConnectionStatus.PENDING){
            throw new BadRequestException(
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
                () -> new ResourceNotFoundException("User not found")
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

    @Override
    public Long connectionCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        List<Connection> connections = connectionRepository.findByUser1OrUser2(currentUser , currentUser);
        return (long) connections.size();
    }

    @Override
    public String removeConnectionById(Long connectionId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        Connection connection = connectionRepository.findById(connectionId).orElseThrow(
                () -> new ResourceNotFoundException("Connection not exists")
        );
        if(!connection.getUser1().getId().equals(currentUser.getId())
        && !connection.getUser2().getId().equals(currentUser.getId())
        ){
            throw new ForbiddenException(
                "You are not authorized to remove this connection"
            );
        }
        connectionRepository.delete(connection);
        return "Connection Deleted Successfully";
    }

    @Override
    public Long pendingRequestCount()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // if exists
        List<ConnectionRequest> connectionRequest = connectionRequestRepository.findByReceiverAndStatus(currentUser,ConnectionStatus.PENDING);
        return (long) connectionRequest.size();
    }

    @Override
    public Long sentRequestCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
        // find By sender
        List<ConnectionRequest> connectionRequests = connectionRequestRepository.findBySender(currentUser);
        return (long) connectionRequests.size();
    }

    @Override
    public List<UserSuggestionResponse> mutualConnection(Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );

        // if currentUser exists then check the userID i wanna get the mutual connection with
        User toFindMutualConnectionWith = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );

        if(currentUser.getId().equals(userId)){
            throw new BadRequestException(
                    "Cannot find mutual connections with yourself"
            );
        }

        // if that user also exists then check weather currentUser has a connection with toFindMutualConnectionWith person only if he has accepted his connectionRequest
        List<Connection> myConnections = connectionRepository.findByUser1OrUser2( // first currentUser will get the all his connection
                currentUser,currentUser
        );

        List<Connection> otherConnections = connectionRepository.findByUser1OrUser2( // i got all the connections of the guy that currentUser want to find the mutual with
                toFindMutualConnectionWith,toFindMutualConnectionWith
        );

        Set<Long> myConnectionIds = new HashSet<>();
        Set<Long> otherConnectionIds = new HashSet<>();

        for (Connection connection : myConnections) {

            if (connection.getUser1().getId()
                    .equals(currentUser.getId())) {

                myConnectionIds.add(
                        connection.getUser2().getId()
                );

            } else {

                myConnectionIds.add(
                        connection.getUser1().getId()
                );
            }
        }

        for (Connection connection : otherConnections) {

            if (connection.getUser1().getId()
                    .equals(toFindMutualConnectionWith.getId())) {

                otherConnectionIds.add(
                        connection.getUser2().getId()
                );

            } else {

                otherConnectionIds.add(
                        connection.getUser1().getId()
                );
            }
        }

        myConnectionIds.retainAll(otherConnectionIds);

        return myConnectionIds.stream()
                .map(id -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "User not found"
                                    )
                            );

                    return UserSuggestionResponse.builder()
                            .id(user.getId())
                            .fullName(user.getFullName())
                            .build();
                })
                .toList();

    }
}
