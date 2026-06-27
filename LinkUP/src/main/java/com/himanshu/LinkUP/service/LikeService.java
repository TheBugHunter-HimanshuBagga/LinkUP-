package com.himanshu.LinkUP.service;

public interface LikeService {
    String likePost(Long postId);

    String unlikePost(Long postId);

    Long likeCount(Long postId);
}
