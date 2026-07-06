package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query(
            """
            SELECT m FROM Message m
            WHERE
                (m.sender.id = :user1 AND m.receiver.id = :user2)
              OR(m.sender.id = :user2 AND m.receiver.id = :user1)
            ORDER BY m.sentAt ASC
            """
    )
    List<Message> findConversation(Long user1, Long user2);
}
/*
findConversation -> search inside the Message and find the notification of both user1 and user2 and get the oldest notifications first
like 10:00 then 10:01 then 10:02 and soo on until the conversation ends
OLDEST -> NEWEST
 */
