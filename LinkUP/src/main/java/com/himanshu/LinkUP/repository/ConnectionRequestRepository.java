package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest , Long> {

    boolean existsBySenderAndReceiver(User sender, User receiver);
    List<ConnectionRequest> findByReceiverAndStatus(User receiver, ConnectionStatus status);
    List<ConnectionRequest> findBySender(User sender);

}
