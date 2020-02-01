package com.hust.baseweb.applications.logistics.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;

public interface InventoryItemRepo extends JpaRepository<InventoryItem,UUID>{
	
}
