package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Activity;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findAllByOrderByCreatedAtDesc(); // displayed on the HOME FEED

    List<Activity> findByUserOrderByCreatedAtDesc(User user); // User Profile Activity

}
