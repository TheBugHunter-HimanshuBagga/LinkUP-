package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.CommentResponse;
import com.himanshu.LinkUP.dto.CreateCommentRequest;

import java.util.List;

public interface CommentService {
    String addComment(Long postId, CreateCommentRequest request);

    List<CommentResponse> getComments(Long postId);

    String deleteComment(Long commentId);
}
