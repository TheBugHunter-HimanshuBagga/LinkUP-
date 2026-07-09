package com.himanshu.LinkUP.service.impl;

import org.junit.jupiter.api.Test;

public class AuthServiceImplTest {

    // Registration
    @Test
    void shouldRegisterUserSuccessfully(){ // email not present -> Encode Password -> save user -> register RegisterResponse

    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){ // Email already exists -> Throw BadRequest

    }

    // Login
    @Test
    void shouldLoginSuccessfully(){ // User found -> password correct -> generate JWT -> return LoginResponse

    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){ // user not found -> resource not found

    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect(){

    }
}
