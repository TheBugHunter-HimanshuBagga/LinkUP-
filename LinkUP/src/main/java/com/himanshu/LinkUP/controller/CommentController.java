package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.CommentResponse;
import com.himanshu.LinkUP.dto.CreateCommentRequest;
import com.himanshu.LinkUP.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long postId, @RequestBody CreateCommentRequest request){
        String message = commentService.addComment(postId,request);
        return ResponseEntity.ok(message);
    }
}
