package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest , Long> {

    boolean existsBySenderAndReceiver(User sender, User receiver);

}
