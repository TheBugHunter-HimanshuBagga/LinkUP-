package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.CommentResponse;
import com.himanshu.LinkUP.dto.CreateCommentRequest;
import com.himanshu.LinkUP.entity.Comment;
import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.NotificationType;
import com.himanshu.LinkUP.exception.ForbiddenException;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.repository.CommentRepository;
import com.himanshu.LinkUP.repository.PostRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.CommentService;
import com.himanshu.LinkUP.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    @Override
    public String addComment(Long postId, CreateCommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        // CurrentUser should be existing to post the comment
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
        // Post to be commented on should alos be existing
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post not found")
                );
        // if all perfect then
        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(currentUser)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        // save this post(comment) inside the CommentRepository
        commentRepository.save(comment);


        if(!post.getAuthor().getId().equals(currentUser.getId())){ //  i won't get any notification if io posted comment on mine post only
            notificationService.createNotification(post.getAuthor(),
                    currentUser.getFullName() + " commented on your post.",
                    NotificationType.POST_COMMENTED);
        }


        return "Comment posted successfully";
    }

    @Override
    public List<CommentResponse> getComments(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post not found")
                );

        return commentRepository.findByPost(post) // since from here i will be getting the list i will break it like [stream().map().toList()]
                .stream()
                .map(comment ->
                        CommentResponse.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getUser().getFullName())
                                .createdAt(comment.getCreatedAt())
                                .build()
                )
                .toList();
    }

    @Override
    public String deleteComment(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        // CurrentUser should be existing to post the comment
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Comment not found")
                );

        if(!comment.getUser().getId().equals(currentUser.getId())){
            throw new ForbiddenException(
                    "You are not authorized to delete this comment"
            );
        }
        commentRepository.delete(comment);
        return "Comment Deleted";
    }
}
