package com.himanshu.LinkUP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "messages")
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne // many messages can be sent by one sender
    @JoinColumn(name = "sender_Id")
    private User sender;

    @ManyToOne // many messages can be received by one receiver
    @JoinColumn(name = "receiver_Id")
    private User receiver;

    private LocalDateTime sentAt;

}
/*
Message
   │
   ├────────► Sender (User)
   │
   └────────► Receiver (User)
 */
