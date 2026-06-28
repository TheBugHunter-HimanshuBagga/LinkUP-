package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.repository.LikeRepository;
import com.himanshu.LinkUP.repository.PostRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public String likePost(Long postId){
        return null;
    }

    @Override
    public String unlikePost(Long postId){
        return null;
    }

    @Override
    public Long likeCount(Long postId){
        return null;
    }

}
