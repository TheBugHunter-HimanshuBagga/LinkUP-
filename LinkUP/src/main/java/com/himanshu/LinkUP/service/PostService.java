package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.CreatePostRequest;
import com.himanshu.LinkUP.dto.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(CreatePostRequest request);
    List<PostResponse> getFeed();
}
