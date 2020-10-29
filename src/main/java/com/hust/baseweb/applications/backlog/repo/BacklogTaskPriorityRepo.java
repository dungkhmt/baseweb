package com.hust.baseweb.applications.backlog.repo;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BacklogTaskPriorityRepo extends JpaRepository<BacklogTaskPriority, String> {
    List<BacklogTaskPriority> findAll();
}
