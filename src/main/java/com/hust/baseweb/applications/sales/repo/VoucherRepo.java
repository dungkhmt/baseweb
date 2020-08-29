package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.Voucher;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface VoucherRepo extends JpaRepository<Voucher, UUID> {

    Voucher findFirstByCode(String code);

    @NotNull
    Page<Voucher> findAll(@NotNull Pageable pageable);
}
