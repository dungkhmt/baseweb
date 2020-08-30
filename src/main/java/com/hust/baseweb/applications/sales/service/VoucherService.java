package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.sales.entity.Voucher;
import com.hust.baseweb.applications.sales.entity.VoucherRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface VoucherService {

    // create
    Voucher createVoucher(Voucher.InputModel inputModel);

    VoucherRule.OutputModel createVoucherRule(UUID voucherId, VoucherRule.InputModel inputModel);

    // read
    List<Voucher> getAllVouchers();

    Page<Voucher> getVouchersPage(Pageable pageable);

    List<VoucherRule.OutputModel> getAllVoucherRules();

    Page<VoucherRule.OutputModel> getVoucherRulesPage(Pageable pageable);

    List<VoucherRule.OutputModel> getAllVoucherRules(UUID voucherId);

    Page<VoucherRule.OutputModel> getVoucherRulesPage(UUID voucherId, Pageable pageable);

    Voucher getVoucher(UUID voucherId);

    VoucherRule.OutputModel getVoucherRule(UUID voucherRuleId);

    // update
    Voucher updateVoucher(UUID voucherId, Voucher.InputModel inputModel);

    VoucherRule.OutputModel updateVoucherRule(UUID voucherRuleId, VoucherRule.InputModel inputModel);

    // delete
    Voucher deleteVoucher(UUID voucherId);

    VoucherRule.OutputModel deleteVoucherRule(UUID voucherRuleId);
}
