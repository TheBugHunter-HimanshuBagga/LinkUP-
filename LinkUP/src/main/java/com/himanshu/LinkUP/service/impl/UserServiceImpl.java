package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.UserResponse;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
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
}