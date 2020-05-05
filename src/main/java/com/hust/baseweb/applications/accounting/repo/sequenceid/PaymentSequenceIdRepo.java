package com.hust.baseweb.applications.accounting.repo.sequenceid;

import com.hust.baseweb.applications.accounting.entity.PaymentSequenceId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface PaymentSequenceIdRepo extends JpaRepository<PaymentSequenceId, Long> {
}
