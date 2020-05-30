package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderHeaderSequenceId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface OrderHeaderSequenceIdRepo extends JpaRepository<OrderHeaderSequenceId, Long> {

}
