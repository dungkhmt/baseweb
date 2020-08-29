package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.Voucher;
import com.hust.baseweb.applications.sales.entity.VoucherRule;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface VoucherRuleRepo extends JpaRepository<VoucherRule, UUID> {

    @NotNull
    Page<VoucherRule> findAll(@NotNull Pageable pageable);

    List<VoucherRule> findAllByVoucher(Voucher voucher);

    @NotNull
    Page<VoucherRule> findAllByVoucher(Voucher voucher, Pageable pageable);


}
