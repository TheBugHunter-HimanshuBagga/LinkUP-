package com.himanshu.LinkUP.entity;

import com.himanshu.LinkUP.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activities")
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @ManyToOne // many activities belongs to one user
    @JoinColumn(name = "user_id")
    private User user;
}
