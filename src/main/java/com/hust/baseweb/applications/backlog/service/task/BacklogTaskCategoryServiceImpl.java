package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskCategory;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskCategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogTaskCategoryServiceImpl implements BacklogTaskCategoryService{

    BacklogTaskCategoryRepo backlogTaskCategoryRepo;

    @Override
    public List<BacklogTaskCategory> findAll() {
        return backlogTaskCategoryRepo.findAll();
    }
}
