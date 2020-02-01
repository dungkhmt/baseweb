package com.hust.baseweb.applications.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.order.entity.SalesChannel;

public interface SalesChannelRepo extends JpaRepository<SalesChannel, String>{
	public SalesChannel findBySalesChannelId(String salesChannelId);
}
