package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;



}
