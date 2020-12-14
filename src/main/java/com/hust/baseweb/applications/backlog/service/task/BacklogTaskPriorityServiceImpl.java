package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskPriority;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskPriorityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BacklogTaskPriorityServiceImpl implements BacklogTaskPriorityService {

    @Autowired
    BacklogTaskPriorityRepo backlogTaskPriorityRepo;

    @Override
    public List<BacklogTaskPriority> findAll() {
        return backlogTaskPriorityRepo.findAll();
    }
}
