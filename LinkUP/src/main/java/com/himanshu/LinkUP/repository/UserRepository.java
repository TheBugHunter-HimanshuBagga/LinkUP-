package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String Email);

    Page<User> findByFullNameContainingIgnoreCaseOrSkillsContainingIgnoreCaseOrInterestsContainingIgnoreCase(
            String keyword,
            String skills,
            String interests,
            Pageable pageable
    );

    Page<User> findByCityIgnoreCase(
            String city,
            Pageable pageable
    );
}