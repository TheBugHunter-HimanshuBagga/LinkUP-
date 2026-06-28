package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likeService(@PathVariable Long postId){
        String message = likeService.likePost(postId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<String> unlikeService(@PathVariable Long postId){
        String message = likeService.unlikePost(postId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<Long> likeCount(@PathVariable Long postId){
        Long count = likeService.likeCount(postId);
        return ResponseEntity.ok(count);
    }

}
