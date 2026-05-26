package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.RegisterRequest;
import com.himanshu.LinkUP.dto.RegisterResponse;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    @Override
    public RegisterResponse response(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email is already existing");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
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

}
