package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.entity.Like;
import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.repository.LikeRepository;
import com.himanshu.LinkUP.repository.PostRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public String likePost(Long postId){

        /*
        Flow ->
        -Authenticate
        -Post should be present this automatically tests the user Posted this
        -currentUser should be present if not throw error
        -if(everyThing is fine then) -> Like the post logic
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("Current User Not Found!")
                );
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new RuntimeException("Post doesn't Exists")
                );

        // if user Liked already
        if(likeRepository.existsByUserAndPost(currentUser,post)){
            throw new RuntimeException("You have already liked this post");
        }

        // if not Liked then build
        Like like = Like.builder()
                .user(currentUser)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
        likeRepository.save(like); // save the like

        return "Post Liked Successfully!";
    }

    @Override
    public String unlikePost(Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("Current User doesn't exists")
                );
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new RuntimeException("Post Not Found")
                );
        // if not liked Already
        if(!likeRepository.existsByUserAndPost(currentUser,post)){
            throw new RuntimeException("You haven't Liked this Post, So U can't unlike this");
        }

        // if Liked
        Like like = likeRepository.findByUserAndPost(currentUser,post).orElseThrow(
                () -> new RuntimeException("Like not found")
        );
        likeRepository.delete(like);
        return "Post Unliked Successfully";
    }

    @Override
    public Long likeCount(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new RuntimeException("Post Not Found😅")
                );
        // if the post is found then
        Long count = likeRepository.countByPost(post);
        return count;
    }

}
