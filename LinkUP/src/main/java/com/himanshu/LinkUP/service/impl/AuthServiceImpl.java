package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.LoginRequest;
import com.himanshu.LinkUP.dto.LoginResponse;
import com.himanshu.LinkUP.dto.RegisterRequest;
import com.himanshu.LinkUP.dto.RegisterResponse;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.exception.BadRequestException;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.exception.UnauthorizedException;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.security.JwtService;
import com.himanshu.LinkUP.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public RegisterResponse response(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestException("Email is already registered"); // 400
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .gender(request.getGender())
                .city(request.getCity())
                .college(request.getCollege())
                .branch(request.getBranch())
                .year(request.getYear())
                .bio(request.getBio())
                .skills(request.getSkills())
                .interests(request.getInterests())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .message("User registered Successfully!!")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found") // 404
        );

        // if user is found then
        boolean isValid = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );
        if(!isValid){
            throw new UnauthorizedException("Invalid email or Password (●'◡'●)"); // 401
        }

        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .message("Login Successful(┬┬﹏┬┬)")
                .build();
    }

}
/*
flow
user comes and enters email if not found email returns usernotfound
if found check password through password encoder by matching request and user password  and check the validation
if valid move forward and generate the token if not throe invalid password
 */