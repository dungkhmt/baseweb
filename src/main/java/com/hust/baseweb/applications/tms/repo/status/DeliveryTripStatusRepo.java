package com.hust.baseweb.applications.tms.repo.status;

import com.hust.baseweb.applications.tms.entity.status.DeliveryTripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface DeliveryTripStatusRepo extends JpaRepository<DeliveryTripStatus, UUID> {
}
