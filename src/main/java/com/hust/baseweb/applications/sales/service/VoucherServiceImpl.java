package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.sales.entity.Voucher;
import com.hust.baseweb.applications.sales.entity.VoucherRule;
import com.hust.baseweb.applications.sales.repo.VoucherRepo;
import com.hust.baseweb.applications.sales.repo.VoucherRuleRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepo voucherRepo;
    private final VoucherRuleRepo voucherRuleRepo;

    public VoucherServiceImpl(
        VoucherRepo voucherRepo,
        VoucherRuleRepo voucherRuleRepo
    ) {
        this.voucherRepo = voucherRepo;
        this.voucherRuleRepo = voucherRuleRepo;
    }

    @Override
    public Voucher create(Voucher.InputModel inputModel) {
        Voucher voucher = voucherRepo.findFirstByCode(inputModel.getCode());
        if (voucher != null) {
            return null;
        }
        return voucherRepo.save(inputModel.toVoucher());
    }

    @Override
    public VoucherRule.OutputModel create(VoucherRule.InputModel inputModel) {
        Optional<Voucher> voucherOptional = voucherRepo.findById(inputModel.getVoucherId());
        return voucherOptional
            .map(voucher -> voucherRuleRepo.save(inputModel.toVoucherRule(uuid -> voucher)).toOutputModel())
            .orElse(null);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepo.findAll();
    }

    @Override
    public Page<Voucher> getVouchersPage(Pageable pageable) {
        return voucherRepo.findAll(pageable);
    }

    @Override
    public List<VoucherRule.OutputModel> getAllVoucherRules() {
        return voucherRuleRepo.findAll().stream().map(VoucherRule::toOutputModel).collect(Collectors.toList());
    }

    @Override
    public Page<VoucherRule.OutputModel> getVoucherRulesPage(Pageable pageable) {
        return voucherRuleRepo.findAll(pageable).map(VoucherRule::toOutputModel);
    }

    @Override
    public List<VoucherRule.OutputModel> getAllVoucherRules(UUID voucherId) {
        Optional<Voucher> voucherOptional = voucherRepo.findById(voucherId);
        return voucherOptional
            .map(voucherRuleRepo::findAllByVoucher)
            .orElse(new ArrayList<>())
            .stream()
            .map(VoucherRule::toOutputModel)
            .collect(Collectors.toList());
    }

    @Override
    public Page<VoucherRule.OutputModel> getVoucherRulesPage(UUID voucherId, Pageable pageable) {
        Optional<Voucher> voucherOptional = voucherRepo.findById(voucherId);
        return voucherOptional
            .map(voucher -> voucherRuleRepo.findAllByVoucher(voucher, pageable))
            .orElse(Page.empty())
            .map(VoucherRule::toOutputModel);
    }

    @Override
    public Voucher getVoucher(UUID voucherId) {
        return voucherRepo.findById(voucherId).orElse(null);
    }

    @Override
    public VoucherRule.OutputModel getVoucherRule(UUID voucherRuleId) {
        return voucherRuleRepo.findById(voucherRuleId).orElse(null).toOutputModel();
    }

    @Override
    public Voucher updateVoucher(UUID voucherId, Voucher.InputModel inputModel) {
        Voucher voucher = voucherRepo.findById(voucherId).orElse(null);
        if (voucher == null) {
            return null;
        }
        inputModel.updateToVoucher(voucher);
        return voucherRepo.save(voucher);
    }


    @Override
    public VoucherRule.OutputModel updateVoucherRule(
        UUID voucherRuleId,
        VoucherRule.InputModel inputModel
    ) {
        VoucherRule voucherRule = voucherRuleRepo.findById(voucherRuleId).orElse(null);
        if (voucherRule == null) {
            return null;
        }
        inputModel.updateToVoucherRule(voucherRule);
        return voucherRuleRepo.save(voucherRule).toOutputModel();
    }

    @Override
    public Voucher deleteVoucher(UUID voucherId) {
        Voucher voucher = voucherRepo.findById(voucherId).orElse(null);
        if (voucher == null) {
            return null;
        }
        voucherRepo.delete(voucher);
        return voucher;
    }

    @Override
    public VoucherRule.OutputModel deleteVoucherRule(UUID voucherRuleId) {
        VoucherRule voucherRule = voucherRuleRepo.findById(voucherRuleId).orElse(null);
        if (voucherRule == null) {
            return null;
        }
        voucherRuleRepo.delete(voucherRule);
        return voucherRule.toOutputModel();
    }
}
