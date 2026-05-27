package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserResponse> discoverUser(
            int page,
            int size
    );
}
