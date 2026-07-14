package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.entity.Like;
import com.himanshu.LinkUP.entity.Post;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.NotificationType;
import com.himanshu.LinkUP.exception.BadRequestException;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.repository.LikeRepository;
import com.himanshu.LinkUP.repository.PostRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LikeServiceImpl likeService;

    @Test
    void shouldLikePostSuccessfully() {

        User currentUser = User.builder()
                .id(1L)
                .fullName("Himanshu")
                .email("himanshu@gmail.com")
                .build();

        User author = User.builder()
                .id(2L)
                .fullName("Rahul")
                .build();

        Post post = Post.builder()
                .id(10L)
                .author(author)
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.of(currentUser));

            when(postRepository.findById(10L))
                    .thenReturn(Optional.of(post));

            when(likeRepository.existsByUserAndPost(currentUser, post))
                    .thenReturn(false);

            String response = likeService.likePost(10L);

            assertEquals(
                    "Post Liked Successfully!",
                    response
            );

            verify(likeRepository)
                    .save(any(Like.class));

            verify(notificationService)
                    .createNotification(
                            eq(author),
                            eq(currentUser.getFullName() + "liked your post."),
                            eq(NotificationType.POST_LIKED)
                    );
        }
    }


    @Test
    void shouldThrowExceptionWhenCurrentUserNotFound() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.empty());

            ResourceNotFoundException exception =
                    assertThrows(
                            ResourceNotFoundException.class,
                            () -> likeService.likePost(1L)
                    );

            assertEquals(
                    "User not found!",
                    exception.getMessage()
            );

            verify(postRepository, never())
                    .findById(anyLong());
        }
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {

        User currentUser = User.builder()
                .id(1L)
                .email("himanshu@gmail.com")
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.of(currentUser));

            when(postRepository.findById(1L))
                    .thenReturn(Optional.empty());

            ResourceNotFoundException exception =
                    assertThrows(
                            ResourceNotFoundException.class,
                            () -> likeService.likePost(1L)
                    );

            assertEquals(
                    "Post not found",
                    exception.getMessage()
            );
        }
    }

    @Test
    void shouldThrowExceptionWhenAlreadyLiked() {

        User currentUser = User.builder()
                .id(1L)
                .email("himanshu@gmail.com")
                .build();

        Post post = Post.builder()
                .id(1L)
                .author(currentUser)
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.of(currentUser));

            when(postRepository.findById(1L))
                    .thenReturn(Optional.of(post));

            when(likeRepository.existsByUserAndPost(currentUser, post))
                    .thenReturn(true);

            BadRequestException exception =
                    assertThrows(
                            BadRequestException.class,
                            () -> likeService.likePost(1L)
                    );

            assertEquals(
                    "You have already liked this post",
                    exception.getMessage()
            );

            verify(likeRepository, never())
                    .save(any());
        }
    }


    @Test
    void shouldUnlikePostSuccessfully() {

        User currentUser = User.builder()
                .id(1L)
                .email("himanshu@gmail.com")
                .build();

        Post post = Post.builder()
                .id(1L)
                .author(currentUser)
                .build();

        Like like = Like.builder()
                .id(1L)
                .user(currentUser)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.of(currentUser));

            when(postRepository.findById(1L))
                    .thenReturn(Optional.of(post));

            when(likeRepository.existsByUserAndPost(currentUser, post))
                    .thenReturn(true);

            when(likeRepository.findByUserAndPost(currentUser, post))
                    .thenReturn(Optional.of(like));

            String response = likeService.unlikePost(1L);

            assertEquals(
                    "Post Unliked Successfully",
                    response
            );

            verify(likeRepository).delete(like);
        }
    }

    @Test
    void shouldThrowExceptionWhenUnlikeWithoutLike() {

        User currentUser = User.builder()
                .id(1L)
                .email("himanshu@gmail.com")
                .build();

        Post post = Post.builder()
                .id(1L)
                .author(currentUser)
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.of(currentUser));

            when(postRepository.findById(1L))
                    .thenReturn(Optional.of(post));

            when(likeRepository.existsByUserAndPost(currentUser, post))
                    .thenReturn(false);

            BadRequestException exception = assertThrows(
                    BadRequestException.class,
                    () -> likeService.unlikePost(1L)
            );

            assertEquals(
                    "You have not liked this post",
                    exception.getMessage()
            );

            verify(likeRepository, never()).delete(any());
        }
    }

    @Test
    void shouldThrowExceptionWhenLikeNotFound() {

        User currentUser = User.builder()
                .id(1L)
                .email("himanshu@gmail.com")
                .build();

        Post post = Post.builder()
                .id(1L)
                .author(currentUser)
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            when(authentication.getName())
                    .thenReturn("himanshu@gmail.com");

            when(userRepository.findByEmail("himanshu@gmail.com"))
                    .thenReturn(Optional.of(currentUser));

            when(postRepository.findById(1L))
                    .thenReturn(Optional.of(post));

            when(likeRepository.existsByUserAndPost(currentUser, post))
                    .thenReturn(true);

            when(likeRepository.findByUserAndPost(currentUser, post))
                    .thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> likeService.unlikePost(1L)
            );

            assertEquals(
                    "Like not found",
                    exception.getMessage()
            );

            verify(likeRepository, never()).delete(any());
        }
    }

    @Test
    void shouldReturnLikeCountSuccessfully() {

        Post post = Post.builder()
                .id(1L)
                .build();

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(likeRepository.countByPost(post))
                .thenReturn(10L);

        Long count = likeService.likeCount(1L);

        assertEquals(
                10L,
                count
        );

        verify(postRepository).findById(1L);

        verify(likeRepository).countByPost(post);
    }


    @Test
    void shouldThrowExceptionWhenCountingLikesForNonExistingPost() {

        when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> likeService.likeCount(1L)
        );

        assertEquals(
                "Post not found",
                exception.getMessage()
        );

        verify(likeRepository, never())
                .countByPost(any(Post.class));
    }


}
