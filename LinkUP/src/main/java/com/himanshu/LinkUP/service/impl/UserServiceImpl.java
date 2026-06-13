package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.UpdateProfileRequest;
import com.himanshu.LinkUP.dto.UserProfileResponse;
import com.himanshu.LinkUP.dto.UserResponse;
import com.himanshu.LinkUP.entity.Connection;
import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.repository.ConnectionRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.AuthService;
import com.himanshu.LinkUP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    @Override
    public Page<UserResponse> discoverUser(int page, int size , String sortBy , String direction) {
        Sort sort =
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page , size , sort);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user ->
                UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .age(user.getAge())
                        .city(user.getCity())
                        .college(user.getCollege())
                        .branch(user.getBranch())
                        .skills(user.getSkills())
                        .interests(user.getInterests())
                        .build()
                );
    }
    @Override
    public Page<UserResponse> searchUsers(String keyword , int page , int size){
        Pageable pageable= PageRequest.of(page , size);
        Page<User> users = userRepository.findByFullNameContainingIgnoreCaseOrSkillsContainingIgnoreCaseOrInterestsContainingIgnoreCase(keyword, keyword , keyword , pageable);
        return users.map(user ->
                UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .age(user.getAge())
                        .city(user.getCity())
                        .college(user.getCollege())
                        .branch(user.getBranch())
                        .skills(user.getSkills())
                        .interests(user.getInterests())
                        .build()
        );
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User Not Found")
        );
        Long connection = (long) connectionRepository.findByUser1OrUser2(user,user).size();
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .connectionCount(connection)
                .build();
    }

    @Override
    public Page<UserResponse> filterUsersByCity(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page , size);
        Page<User> users = userRepository.findByCityIgnoreCase(
                city,
                pageable
        );
        return users.map(user ->
                UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .age(user.getAge())
                        .city(user.getCity())
                        .college(user.getCollege())
                        .branch(user.getBranch())
                        .skills(user.getSkills())
                        .interests(user.getInterests())
                        .build()
                );


    }

    @Override
    public UserProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User do not exists"));
        // if user exists then , let them put the data
        // if user just want one field to be updated
        if(updateProfileRequest.getBio() != null){
            currentUser.setBio(updateProfileRequest.getBio());
        }

        if(updateProfileRequest.getSkills() != null){
            currentUser.setSkills(updateProfileRequest.getSkills());
        }

        if(updateProfileRequest.getInterests() != null){
            currentUser.setInterests(updateProfileRequest.getInterests());
        }

        if(updateProfileRequest.getCity() != null){
            currentUser.setCity(updateProfileRequest.getCity());
        }
        User savedUser = userRepository.save(currentUser);

        Long connectionCount = (long) connectionRepository.findByUser1OrUser2(savedUser,savedUser).size();

        return UserProfileResponse.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .connectionCount(connectionCount)
                .build();
    }

}