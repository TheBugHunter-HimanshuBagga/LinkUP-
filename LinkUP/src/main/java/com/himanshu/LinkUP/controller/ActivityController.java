package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.ActivityResponse;
import com.himanshu.LinkUP.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getActivityFeed() {

        List<ActivityResponse> activityFeed =
                activityService.getActivityFeed();

        return ResponseEntity.ok(activityFeed);
    }

}