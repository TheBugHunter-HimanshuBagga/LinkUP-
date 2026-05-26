package com.himanshu.LinkUP.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private Integer age;

    private String gender;

    private String city;

    private String college;

    private String branch;

    private Integer year;

    @Column(length = 1000)
    private String bio;

    private String skills;

    private String interests;

    private String profilePictureUrl;

    private LocalDateTime createdAt;
}
