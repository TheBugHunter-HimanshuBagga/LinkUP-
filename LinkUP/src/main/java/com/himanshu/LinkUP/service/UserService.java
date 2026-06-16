package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.*;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<UserResponse> discoverUser(
            int page,
            int size,
            String sortBy,
            String direction
    );
    Page<UserResponse> searchUsers(
            String keyword,
            int page,
            int size
    );
    Page<UserResponse> filterUsersByCity(
            String city,
            int page,
            int size
    );

    UserProfileResponse getUserProfile(Long userId);

    UserProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest);

    List<UserSuggestionResponse> getSuggestions();


    UserProfileResponse getMyProfile();

    UserStatsResponse getUserStats();
}
