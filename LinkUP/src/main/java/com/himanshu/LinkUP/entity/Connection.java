package com.himanshu.LinkUP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "connections")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    User user1; // Many connections belongs to user1

    @ManyToOne
    @JoinColumn(name = "user2_id")
    User user2; // Many connections belong to user2

    LocalDateTime connectedAt;
}
