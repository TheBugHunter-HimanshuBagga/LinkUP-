package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.CreatePostRequest;
import com.himanshu.LinkUP.dto.PostResponse;
import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ActivityType;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.repository.ActivityRepository;
import com.himanshu.LinkUP.repository.PostRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ActivityService activityService;

    @InjectMocks
    private PostServiceImpl postService;

    // create Post
    // should create post successfully
    // should throw exception when user not found


    @Test
    void shouldCreatePostSuccessfully() {

        // Arrange
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Hello LinkUP!");

        User currentUser = User.builder()
                .id(1L)
                .fullName("Himanshu")
                .email("himanshu@gmail.com")
                .build();

        Post savedPost = Post.builder()
                .id(1L)
                .content(request.getContent())
                .author(currentUser)
                .createdAt(LocalDateTime.now())
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = mockStatic(SecurityContextHolder.class)) {

            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.of(currentUser));

            when(postRepository.save(any(Post.class)))
                    .thenReturn(savedPost);

            // Act
            PostResponse response = postService.createPost(request);

            // Assert
            assertNotNull(response);

            assertEquals(savedPost.getId(), response.getId());
            assertEquals(savedPost.getContent(), response.getContent());
            assertEquals(currentUser.getFullName(), response.getAuthorName());

            // Verify
            verify(userRepository)
                    .findByEmail("himanshu@gmail.com");

            verify(postRepository)
                    .save(any(Post.class));

            verify(activityService)
                    .createActivity(
                            eq(currentUser),
                            eq("Created a new post."),
                            eq(ActivityType.POST_CREATED)
                    );
        }
    }

    @Test
    void shouldThrowExceptionWhenCurrentUserNotFound() {

        // Arrange
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Hello LinkUP!");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = mockStatic(SecurityContextHolder.class)) {

            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.empty());

            // Act & Assert
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> postService.createPost(request)
            );

            assertEquals(
                    "User not found",
                    exception.getMessage()
            );

            // Verify
            verify(userRepository)
                    .findByEmail("himanshu@gmail.com");

            verify(postRepository, never())
                    .save(any(Post.class));

            verify(activityService, never())
                    .createActivity(
                            any(User.class),
                            anyString(),
                            any(ActivityType.class)
                    );
        }
    }


    @Test
    void shouldReturnAllPostsSuccessfully() {

        // Arrange
        User user = User.builder()
                .id(1L)
                .fullName("Himanshu")
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .content("First Post")
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .content("Second Post")
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();

        when(postRepository.findAll())
                .thenReturn(List.of(post1, post2));

        // Act
        List<PostResponse> response = postService.getFeed();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("First Post", response.get(0).getContent());
        assertEquals("Second Post", response.get(1).getContent());

        // Verify
        verify(postRepository).findAll();
    }

    @Test
    void shouldReturnPostsByUserSuccessfully() {

        // Arrange
        User user = User.builder()
                .id(1L)
                .fullName("Himanshu")
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("Learning Mockito")
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(postRepository.findByAuthor(user))
                .thenReturn(List.of(post));

        // Act
        List<PostResponse> response = postService.getPostsByUser(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(
                "Learning Mockito",
                response.get(0).getContent()
        );

        assertEquals(
                "Himanshu",
                response.get(0).getAuthorName()
        );

        // Verify
        verify(userRepository).findById(1L);

        verify(postRepository)
                .findByAuthor(user);
    }
    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> postService.getPostsByUser(1L)
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findById(1L);

        verify(postRepository, never())
                .findByAuthor(any(User.class));
    }

}
