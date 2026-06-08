package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.MyConnectionResponse;
import com.himanshu.LinkUP.entity.Connection;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.repository.ConnectionRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
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
}
