package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.NotificationResponse;
import com.himanshu.LinkUP.entity.Notification;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.NotificationType;
import com.himanshu.LinkUP.exception.ForbiddenException;
import com.himanshu.LinkUP.exception.ResourceNotFoundException;
import com.himanshu.LinkUP.repository.NotificationRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    @Override
    public void createNotification(User recipient, String message , NotificationType type) {
        // no need for authentication for this
        Notification notification = Notification.builder()
                .message(message)
                .isRead(false) // when notification is created it won't be read by anyone
                .createdAt(LocalDateTime.now())
                .type(type)
                .recipient(recipient)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getMyNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );

        // no if the currentUser is existing then
        return notificationRepository
                .findByRecipientOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(
                        notification ->
                                NotificationResponse.builder()
                                        .id(notification.getId())
                                        .message( notification.getMessage())
                                        .isRead(notification.isRead())
                                        .type(notification.getType())
                                        .createdAt(notification.getCreatedAt())
                                        .build()
                )
                .toList();
    }

    @Override
    public String markAsRead(Long notificationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        // User should be present
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );

        // to marked as read notifications should be present
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Notification not found")
                );

        // if everuthing is fine hence
        if(!notification.getRecipient().getId().equals(currentUser.getId())){
            throw new ForbiddenException("You are not authorized to access this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);

        return "Notification marked as read ⚡";
    }

    @Override
    public String deleteNotification(Long notificationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Notification not found")
                );

        // does this notification belongs to the logged-in user
        if(!notification.getRecipient().getId().equals(currentUser.getId())){
            throw new ForbiddenException("You are not authorized to delete this notification");
        }

        // if existing then
        notificationRepository.delete(notification);
        return "Notification deleted successfully 🙌";
    }
}
