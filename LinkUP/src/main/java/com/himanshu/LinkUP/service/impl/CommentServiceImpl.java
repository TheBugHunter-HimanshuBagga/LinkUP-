package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.CommentResponse;
import com.himanshu.LinkUP.dto.CreateCommentRequest;
import com.himanshu.LinkUP.repository.CommentRepository;
import com.himanshu.LinkUP.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public String addComment(Long postId, CreateCommentRequest request) {
        return "";
    }

    @Override
    public List<CommentResponse> getComments(Long postId) {
        return List.of();
    }

    @Override
    public String deleteComment(Long commentId) {
        return "";
    }
}
