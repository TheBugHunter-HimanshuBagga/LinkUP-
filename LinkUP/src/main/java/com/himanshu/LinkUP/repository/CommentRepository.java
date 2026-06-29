package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Comment;
import com.himanshu.LinkUP.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);
}
