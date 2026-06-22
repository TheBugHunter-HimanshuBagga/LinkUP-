package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);
}
