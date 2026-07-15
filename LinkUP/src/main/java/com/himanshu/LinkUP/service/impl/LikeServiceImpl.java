package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.entity.Like;
import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.NotificationType;
import com.himanshu.LinkUP.exception.BadRequestException;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.repository.LikeRepository;
import com.himanshu.LinkUP.repository.PostRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.LikeService;
import com.himanshu.LinkUP.service.NotificationService;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

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
                        () -> new ResourceNotFoundException("User not found!")
                );
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post not found")
                );

        // if user Liked already
        if(likeRepository.existsByUserAndPost(currentUser,post)){
            throw new BadRequestException("You have already liked this post");
        }

        // if not Liked then build
        Like like = Like.builder()
                .user(currentUser)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
        likeRepository.save(like); // save the like

        if(!post.getAuthor().getId().equals(currentUser.getId())){ // i won't get the notification when i liked my own post
            notificationService.createNotification(post.getAuthor(),
                    currentUser.getFullName() + "liked your post." ,
                    NotificationType.POST_LIKED);
        }

        return "Post Liked Successfully!";
    }

    @Override
    public String unlikePost(Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post not found")
                );
        // if not liked Already
        if(!likeRepository.existsByUserAndPost(currentUser,post)){
            throw new BadRequestException("You have not liked this post");
        }

        // if Liked
        Like like = likeRepository.findByUserAndPost(currentUser,post).orElseThrow(
                () -> new ResourceNotFoundException("Like not found")
        );
        likeRepository.delete(like);
        return "Post Unliked Successfully";
    }

    @Override
    public Long likeCount(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post not found")
                );
        // if the post is found then
        Long count = likeRepository.countByPost(post);
        return count;
    }

}
