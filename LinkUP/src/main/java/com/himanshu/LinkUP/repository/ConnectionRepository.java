package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Connection;
import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection , Long> {
    List<Connection> findByUser1OrUser2(User user1, User user2);
}
