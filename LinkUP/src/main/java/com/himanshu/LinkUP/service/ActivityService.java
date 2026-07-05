package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.ActivityResponse;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ActivityType;

import java.util.List;

public interface ActivityService {
    void createActivity(
            User user,
            String message,
            ActivityType type
    );

    List<ActivityResponse> getActivityFeed();
}
