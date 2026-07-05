package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.NotificationResponse;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.NotificationType;
import com.himanshu.LinkUP.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void createNotification(User recipient, String message , NotificationType type) {

    }

    @Override
    public List<NotificationResponse> getMyNotifications() {
        return null;
    }

    @Override
    public String markAsRead(Long notificationId) {
        return "";
    }

    @Override
    public String deleteNotification(Long notificationId) {
        return "";
    }
}
