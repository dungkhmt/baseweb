package com.hust.baseweb.applications.accounting.repo.sequenceid;

import com.hust.baseweb.applications.accounting.entity.InvoiceSequenceId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceSequenceIdRepo extends JpaRepository<InvoiceSequenceId, Long> {
}
