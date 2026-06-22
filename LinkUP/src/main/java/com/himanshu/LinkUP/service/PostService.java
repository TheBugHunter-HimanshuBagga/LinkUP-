package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.CreatePostRequest;
import com.himanshu.LinkUP.dto.PostResponse;

public interface PostService {
    PostResponse createPost(CreatePostRequest request);
}
