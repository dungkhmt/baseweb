package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.SalesChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesChannelRepo extends JpaRepository<SalesChannel, String> {
    SalesChannel findBySalesChannelId(String salesChannelId);
}
