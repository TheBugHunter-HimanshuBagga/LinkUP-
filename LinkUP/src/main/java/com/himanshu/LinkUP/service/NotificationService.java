package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.NotificationResponse;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.NotificationType;

import java.util.List;

public interface NotificationService{
    void createNotification(
            User recipient,
            String message,
            NotificationType type
    );

    List<NotificationResponse> getMyNotifications();

    String markAsRead(Long notificationId);

    String deleteNotification(Long notificationId);
}
/*
Since my createNotification is an internal Service called by
LikeService and CommentService will call it
No controller will expose this directly
 */