package com.hust.baseweb.applications.backlog.service;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskCategory;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BacklogTaskCategoryServiceImpl implements BacklogTaskCategoryService{

    @Autowired
    BacklogTaskCategoryRepo backlogTaskCategoryRepo;

    @Override
    public List<BacklogTaskCategory> findAll() {
        return backlogTaskCategoryRepo.findAll();
    }
}
