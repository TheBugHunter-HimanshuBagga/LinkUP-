package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Notification;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification , Long> {

   List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

}
/*
Newest  ->  Oldest
 */