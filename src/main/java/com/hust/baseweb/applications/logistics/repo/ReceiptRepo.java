package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface ReceiptRepo extends JpaRepository<Receipt, String> {
}
