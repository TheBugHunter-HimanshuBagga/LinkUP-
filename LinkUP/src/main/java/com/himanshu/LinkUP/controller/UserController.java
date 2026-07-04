package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.*;
import com.himanshu.LinkUP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.ListType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(){
        UserProfileResponse userProfileResponse = userService.getMyProfile();
        return ResponseEntity.ok(userProfileResponse);
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

    @GetMapping("/suggestions")
    public List<UserSuggestionResponse> getSuggestions(){
        List<UserSuggestionResponse> userSuggestionResponse = userService.getSuggestions();
        return userSuggestionResponse;
    }

    @GetMapping("/stats")
    public ResponseEntity<UserStatsResponse> getUserStats(){
        UserStatsResponse userStatsResponse = userService.getUserStats();
        return ResponseEntity.ok(userStatsResponse);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file){ // Take the part named as file from the request
        String message = userService.uploadProfilePicture(file);
        return ResponseEntity.ok(message);
    }

    // Adding resume
    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file){
        String message = userService.uploadResume(file);
        return ResponseEntity.ok(message);
    }
}


/*
/api/users/discover
 */
