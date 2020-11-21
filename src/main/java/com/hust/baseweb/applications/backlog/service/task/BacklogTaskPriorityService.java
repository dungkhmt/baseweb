package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskPriority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BacklogTaskPriorityService {
    List<BacklogTaskPriority> findAll();
}
