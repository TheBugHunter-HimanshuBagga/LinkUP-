package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.CreatePostRequest;
import com.himanshu.LinkUP.dto.PostResponse;
import com.himanshu.LinkUP.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest request){
        PostResponse postResponse = postService.createPost(request);
        return ResponseEntity.ok(postResponse);
    }


}
