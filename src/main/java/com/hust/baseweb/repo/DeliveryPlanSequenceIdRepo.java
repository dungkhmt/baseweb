package com.hust.baseweb.repo;

import com.hust.baseweb.applications.tms.entity.sequenceid.DeliveryPlanSequenceId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface DeliveryPlanSequenceIdRepo extends JpaRepository<DeliveryPlanSequenceId, Long> {

}
