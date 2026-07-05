package com.himanshu.LinkUP.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private String resumeUrl;

    @OneToMany(mappedBy = "uploadedBy" , cascade = CascadeType.ALL)  //One user has many documents
    private List<Document> documentList; // fetch all documents of the particular user fetch it

    @OneToMany(mappedBy = "recipient" , cascade = CascadeType.ALL) // one user can get many notifications
    private List<Notification> notification; // fetch all notifications of the particular user using bidirectional relationships
}
