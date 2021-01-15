package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskPriority;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskPriorityRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogTaskPriorityServiceImpl implements BacklogTaskPriorityService {

    BacklogTaskPriorityRepo backlogTaskPriorityRepo;

    @Override
    public List<BacklogTaskPriority> findAll() {
        return backlogTaskPriorityRepo.findAll();
    }
}
