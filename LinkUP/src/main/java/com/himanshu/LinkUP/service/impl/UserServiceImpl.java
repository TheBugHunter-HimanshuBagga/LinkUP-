package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.*;
import com.himanshu.LinkUP.entity.Connection;
import com.himanshu.LinkUP.entity.ConnectionRequest;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ConnectionStatus;
import com.himanshu.LinkUP.repository.ConnectionRepository;
import com.himanshu.LinkUP.repository.ConnectionRequestRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.AuthService;
import com.himanshu.LinkUP.service.ConnectionRequestService;
import com.himanshu.LinkUP.service.ConnectionService;
import com.himanshu.LinkUP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
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

    @Override
    public List<UserSuggestionResponse> getSuggestions() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User does not exist"
                                )
                        );

        List<User> allUsers = userRepository.findAll();

        Set<Long> excludedUserIds = new HashSet<>();

        // Exclude current user
        excludedUserIds.add(currentUser.getId());

        // Exclude connected users
        List<Connection> connections =
                connectionRepository.findByUser1OrUser2(
                        currentUser,
                        currentUser
                );

        for (Connection connection : connections) {

            if (connection.getUser1()
                    .getId()
                    .equals(currentUser.getId())) {

                excludedUserIds.add(
                        connection.getUser2().getId()
                );

            } else {

                excludedUserIds.add(
                        connection.getUser1().getId()
                );
            }
        }

        // Exclude users having requests with current user
        List<ConnectionRequest> requests =
                connectionRequestRepository
                        .findBySenderOrReceiver(
                                currentUser,
                                currentUser
                        );

        for (ConnectionRequest request : requests) {

            excludedUserIds.add(
                    request.getSender().getId()
            );

            excludedUserIds.add(
                    request.getReceiver().getId()
            );
        }

        return allUsers.stream()
                .filter(user ->
                        !excludedUserIds.contains(
                                user.getId()
                        )
                )
                .map(user ->
                        UserSuggestionResponse.builder()
                                .id(user.getId())
                                .fullName(user.getFullName())
                                .build()
                )
                .toList();
    }

    @Override
    public UserProfileResponse getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User doesn't exists")
                );

        Long connectionCount = (long) connectionRepository.findByUser1OrUser2(currentUser,currentUser).size();
        // if exists then
        // return UserProfileResponseBuilder
        return UserProfileResponse.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .connectionCount(connectionCount)
                .build();
    }

    @Override
    public UserStatsResponse getUserStats(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("currentUser doesn't exists")
                );

        Long connectionCount = (long) connectionRepository.findByUser1OrUser2(currentUser,currentUser).size();

        Long pendingRequestCount = (long) connectionRequestRepository.findByReceiverAndStatus(currentUser, ConnectionStatus.PENDING).size();

        Long sentRequestCount = (long) connectionRequestRepository.findBySender(currentUser).size();
        // UserStatsResponse builder
        return UserStatsResponse.builder()
                .connectionCount(connectionCount)
                .pendingRequestCount(pendingRequestCount)
                .sentRequestCount(sentRequestCount)
                .build();
    }

    @Override
    public String uploadProfilePicture(MultipartFile file) {

        // Check if file is uploaded
        if (file.isEmpty()) {
            throw new RuntimeException("No file selected");
        }

        // Authenticate current user
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        // Generate unique file name
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        // Upload folder
        Path uploadPath = Paths.get("uploads/profile");

        try {

            // Create folder if it doesn't exist
            Files.createDirectories(uploadPath);

            // Copy file from request to uploads/profile
            Files.copy(
                    file.getInputStream(),
                    uploadPath.resolve(fileName)
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }

        // Save image path in database
        currentUser.setProfilePictureUrl(
                "uploads/profile/" + fileName
        );

        userRepository.save(currentUser);

        return "Profile picture uploaded successfully";
    }
}