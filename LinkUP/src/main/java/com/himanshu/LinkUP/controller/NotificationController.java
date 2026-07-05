package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.NotificationResponse;
import com.himanshu.LinkUP.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(){
        List<NotificationResponse> notificationResponses = notificationService.getMyNotifications();
        return ResponseEntity.ok(notificationResponses);
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId){
        String message = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotifications(@PathVariable Long notificationId){
        String message = notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(message);
    }
}
