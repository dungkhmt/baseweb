package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BacklogTaskCategoryService {
    List<BacklogTaskCategory> findAll();
}
