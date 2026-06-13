package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.UpdateProfileRequest;
import com.himanshu.LinkUP.dto.UserProfileResponse;
import com.himanshu.LinkUP.dto.UserResponse;
import com.himanshu.LinkUP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/discovery")
    public Page<UserResponse> discoverUser(@RequestParam(defaultValue = "0") int page ,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(defaultValue = "asc") String direction){
        Page<UserResponse> userResponse = userService.discoverUser(page , size , sortBy , direction);
        return userResponse;
    }

    @GetMapping("/discovery/search")
    public Page<UserResponse> searchUser(@RequestParam String keyword,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int size){
        Page<UserResponse> userResponses = userService.searchUsers(keyword , page , size);
        return userResponses;
    }

    @GetMapping("/discovery/filter")
    public Page<UserResponse> filterByCity(@RequestParam String city,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size){
        Page<UserResponse> userResponses = userService.filterUsersByCity(city , page , size);
        return userResponses;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId){
        UserProfileResponse userProfileResponse = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfileResponse);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest){
        UserProfileResponse userProfileResponse = userService.updateProfile(updateProfileRequest);
        return ResponseEntity.ok(userProfileResponse);
    }
}


/*
/api/users/discover
 */
