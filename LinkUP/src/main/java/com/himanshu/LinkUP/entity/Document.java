package com.himanshu.LinkUP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String fileUrl;

    private LocalDateTime uploadedAt;

    @ManyToOne // Many documents belongs to one user
    @JoinColumn(name = "user_id")
    private User uploadedBy;
}
