package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.RegisterRequest;
import com.himanshu.LinkUP.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse response(RegisterRequest request);
}
