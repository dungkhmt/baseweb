package com.hust.baseweb.repo;

import com.hust.baseweb.applications.tms.entity.sequenceid.DeliveryTripSequenceId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface DeliveryTripSequenceIdRepo extends JpaRepository<DeliveryTripSequenceId, Long> {

}
