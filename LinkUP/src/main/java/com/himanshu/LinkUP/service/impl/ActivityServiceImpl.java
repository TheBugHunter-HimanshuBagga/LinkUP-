package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.ActivityResponse;
import com.himanshu.LinkUP.entity.Activity;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ActivityType;
import com.himanshu.LinkUP.repository.ActivityRepository;
import com.himanshu.LinkUP.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService  {
    private final ActivityRepository activityRepository;

    @Override
    public void createActivity(User user, String message, ActivityType type) {
        Activity activity = Activity.builder()
                .user(user)
                .message(message)
                .type(type)
                .createdAt(LocalDateTime.now())
                .build();
        activityRepository.save(activity);
    }

    @Override
    public List<ActivityResponse> getActivityFeed() {

        return activityRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(activity ->
                        ActivityResponse.builder()
                                .id(activity.getId())
                                .userName(activity.getUser().getFullName())
                                .message(activity.getMessage())
                                .type(activity.getType())
                                .localDateTime(activity.getCreatedAt())
                                .build()
                )
                .toList();
    }
}
