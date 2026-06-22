package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.CreatePostRequest;
import com.himanshu.LinkUP.dto.PostResponse;
import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.repository.PostRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Override
    public PostResponse createPost(CreatePostRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("Current User not Found")
                );

        // if the user is found in the DB then make a builder entity hence save it inside the github
        Post post = Post.builder()
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .author(currentUser)
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponse.builder()
                .id(savedPost.getId())
                .content(savedPost.getContent())
                .authorName(savedPost.getAuthor().getFullName())
                .createdAt(savedPost.getCreatedAt())
                .build();
    }
}
