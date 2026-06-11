package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.UserProfileResponse;
import com.himanshu.LinkUP.dto.UserResponse;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.domain.Page;

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

}
