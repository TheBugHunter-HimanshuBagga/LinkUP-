package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Like;
import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<Like> findByUserAndPost(User user, Post post);

    Long countByPost(Post post);
}