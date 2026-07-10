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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock // fake userRepository object
    private UserRepository userRepository;

    @Mock // fake Password Encoder
    private PasswordEncoder passwordEncoder;

    @Mock // fake JWT service
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    // Registration
    @Test
    void shouldRegisterUserSuccessfully(){ // email not present -> Encode Password -> save user -> register RegisterResponse
        //
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Himanshu");
        request.setEmail("himanshu@gmail.com");
        request.setPassword("password123");
        request.setAge(20);
        request.setGender("Male");
        request.setCity("Delhi");
        request.setCollege("SRM");
        request.setBranch("CSE");
        request.setYear(3);
        request.setBio("Java Backend Developer");
        request.setSkills("Java, Spring Boot");
        request.setInterests("Backend Development");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(
                Optional.empty()
        );

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");

        User savedUser = User.builder()
                .id(1L)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password("encoded-password")
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

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        RegisterResponse response = authService.response(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("Himanshu", response.getFullName());
        assertEquals("himanshu@gmail.com", response.getEmail());
        assertEquals("User registered Successfully!!", response.getMessage());

        // Verify
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){ // Email already exists -> Throw BadRequest
        RegisterRequest request = new RegisterRequest();
        request.setEmail("himanshu@gmail.com");

        User extingUser = User.builder()
                .id(1L)
                .email("himanshu@gmail.com")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(
                Optional.of(extingUser)
        );

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.response(request)
        );

        assertEquals(
                "Email is already registered",
                exception.getMessage()
        );

        // Verify
        verify(userRepository).findByEmail(request.getEmail());

        // These should never be called
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    // Login
    @Test
    void shouldLoginSuccessfully(){ // User found -> password correct -> generate JWT -> return LoginResponse
        LoginRequest request = new LoginRequest(); //
        request.setEmail("himanshu@gmail.com");
        request.setPassword("password123");

        User user = User.builder()
                .id(1L)
                .fullName("Himanshu")
                .email("himanshu@gmail.com")
                .password("password123")
                .build();


        when(userRepository.findByEmail(request.getEmail())).thenReturn(
                Optional.of(user)
        );

        when(passwordEncoder.matches(
            request.getPassword(),
            user.getPassword()
        )).thenReturn(
                true
        );

        when(jwtService.generateToken(user)).thenReturn(
                "fake-jwt-token"
        );

        LoginResponse response = authService.login(request);

        // Response should not be null
        assertNotNull(response);

        // Token should match
        assertEquals(
                "fake-jwt-token",
                response.getToken()
        );

        // Login message should match
        assertEquals(
                "Login Successful(┬┬﹏┬┬)",
                response.getMessage()
        );


        // verify -> to check that all the dependencies were actually called
        verify(userRepository)
                .findByEmail(request.getEmail());

        verify(passwordEncoder)
                .matches(
                        request.getPassword(),
                        user.getPassword()
                );

        verify(jwtService)
                .generateToken(user);

        System.out.println("Login Successfully");

    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){ // user not found -> resource not found
        LoginRequest request = new LoginRequest();
        request.setEmail("himanshu@gmail.com");
        request.setPassword("password@123");

        // no user exists

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authService.login(request)
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        // VERIFY
        verify(userRepository).findByEmail(request.getEmail());

        verify(passwordEncoder, never()).matches(anyString(),anyString());

        verify(jwtService, never()).generateToken(any(User.class));

    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect(){
        LoginRequest request = new LoginRequest();
        request.setEmail("himanshu@gmail.com");
        request.setPassword("password@123");

        User user = User.builder()
                .id(1L)
                .fullName("Himanshu")
                .email("himanshu@gmail.com")
                .password("encoded-password")
                .build();


        when(userRepository.findByEmail(request.getEmail())).thenReturn(
                Optional.of(user)
        );

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )).thenReturn(false);

        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authService.login(request)
        );

        assertEquals(
                "Invalid email or password (●'◡'●)",
                exception.getMessage()
        );

        verify(userRepository).findByEmail(request.getEmail());

        verify(passwordEncoder).matches(
                request.getPassword(),
                user.getPassword()
        );

        verify(jwtService , never()).generateToken( // since password is wrong hence JWT is never generated
                any(User.class)
        );
    }
}

/*
@Mock -> create fake object of dependencies
@InjectMocks -> Creates the object under test and injects all the mocked dependencies into it automatically.

 WHEN  ->
            if -> this method is called -> return this value
 assert ->
            assertEquals , assertThrows
 Verify -?

 */