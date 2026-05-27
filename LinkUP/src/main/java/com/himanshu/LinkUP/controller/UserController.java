package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.UserResponse;
import com.himanshu.LinkUP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/discovery")
    public Page<UserResponse> discoverUser(@RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "5") int size){
        Page<UserResponse> userResponse = userService.discoverUser(page , size);
        return userResponse;
    }
}


/*
/api/users/discover
 */
