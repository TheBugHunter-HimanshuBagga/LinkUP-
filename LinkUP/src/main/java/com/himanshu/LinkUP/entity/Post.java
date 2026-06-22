package com.himanshu.LinkUP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne // Many posts can be created by 1 author
    @JoinColumn(name = "author_id")
    private User author;
}
