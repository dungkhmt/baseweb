package com.hust.baseweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.entity.StatusItem;

public interface StatusItemRepo extends JpaRepository<StatusItem, String> {
	StatusItem findByStatusId(String statusId);
}
