package com.hust.baseweb.repo;

import com.hust.baseweb.entity.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusItemRepo extends JpaRepository<StatusItem, String> {

    StatusItem findByStatusId(String statusId);
}
