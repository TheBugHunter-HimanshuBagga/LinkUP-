package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.PendingRequestResponse;
import com.himanshu.LinkUP.entity.Connection;
import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ActivityType;
import com.himanshu.LinkUP.enums.ConnectionStatus;
import com.himanshu.LinkUP.enums.NotificationType;
import com.himanshu.LinkUP.exception.BadRequestException;
import com.himanshu.LinkUP.exception.ForbiddenException;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.repository.ConnectionRepository;
import com.himanshu.LinkUP.repository.ConnectionRequestRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ActivityService;
import com.himanshu.LinkUP.service.ConnectionRequestService;
import com.himanshu.LinkUP.service.NotificationService;
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
    private final ConnectionRepository connectionRepository;
    private final NotificationService notificationService;
    private final ActivityService activityService;
    @Override
    public void sendRequest(Long receiverId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User sender = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User Not Exists")
        );
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver not found")
                        );
        // what if the user send request to himself
        if(sender.getId().equals(receiver.getId())){
            throw new BadRequestException(
                    "You can't send request to yourself"
            );
        }
        if(connectionRequestRepository.existsBySenderAndReceiver(sender , receiver)){
            throw new BadRequestException(
                    "Connection request already sent"
            );
        }
        ConnectionRequest request = ConnectionRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(ConnectionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        connectionRequestRepository.save(request);

        notificationService.createNotification(receiver,
                sender.getFullName() + " sent you a connection request",
                NotificationType.CONNECTION_REQUEST);
    }


    @Override
    public List<PendingRequestResponse> pendingRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User receiver = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
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
    public String acceptPendingRequest(Long requestId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Not Found")
                        );
        ConnectionRequest request = connectionRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Connection request not found")
                        );
        if(!request.getReceiver().getId().equals(currentUser.getId())){
            throw new ForbiddenException(
                    "You are not authorized to accept the request"
            );
        }
        if(request.getStatus() != ConnectionStatus.PENDING){
            throw new BadRequestException(
                    "Request has already been processed"
            );
        }
        request.setStatus(ConnectionStatus.ACCEPTED);
        Connection connection = Connection.builder()
                .user1(request.getSender())
                .user2(request.getReceiver())
                .connectedAt(LocalDateTime.now())
                .build();
        connectionRequestRepository.save(request);
        connectionRepository.save(connection);

        activityService.createActivity(
                currentUser,
                "Connected with: " + request.getSender().getFullName(),
                ActivityType.CONNECTION_CREATED
        );

        notificationService.createNotification(request.getSender(),
                currentUser.getFullName() + " accepted your connection request",
                NotificationType.CONNECTION_ACCEPTED);

        return "Request Accepted Successfully";
    }

    @Override
    public String rejectPendingRequest(Long receiverId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Himank
        String email = authentication.getName(); // HImank
        User currentUser = userRepository.findByEmail(email).orElseThrow(// Himank
                () -> new ResourceNotFoundException("User not found")
        );
        ConnectionRequest request = connectionRequestRepository.findById(receiverId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Connection request not found") // himanshu request found
                );
        if(!request.getReceiver().getId().equals(currentUser.getId())){ // HITTT
            throw new ForbiddenException(
                    "You are not authorized to reject the request"
            );
        }
        // check weather the status is pending
        if(request.getStatus() != ConnectionStatus.PENDING){
            throw new BadRequestException(
                    "Request is already processed"
            );
        }
        request.setStatus(ConnectionStatus.REJECTED);
        connectionRequestRepository.save(request);
        return "Request has been rejected™";
    }

    @Override
    public List<PendingRequestResponse> latestPendingRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
        List<ConnectionRequest> connectionRequests = connectionRequestRepository.findByReceiverAndStatus(currentUser,ConnectionStatus.PENDING);

        return connectionRequests.stream()
                .limit(3) // only  3 latest request we will see
                .map(connectionRequest ->
                        PendingRequestResponse.builder()
                                .requestId(connectionRequest.getId())
                                .senderName(connectionRequest.getSender().getFullName())
                                .build()
                )
                .toList();
    }

}
