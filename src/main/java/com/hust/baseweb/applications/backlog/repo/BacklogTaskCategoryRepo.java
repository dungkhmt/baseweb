package com.hust.baseweb.applications.backlog.repo;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BacklogTaskCategoryRepo extends JpaRepository<BacklogTaskCategory, String> {
    List<BacklogTaskCategory> findAll();
}
