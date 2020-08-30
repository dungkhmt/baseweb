package com.hust.baseweb.applications.sales.controller;

import com.hust.baseweb.applications.sales.entity.Voucher;
import com.hust.baseweb.applications.sales.entity.VoucherRule;
import com.hust.baseweb.applications.sales.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@RestController
@CrossOrigin
public class VoucherApiController {

    private final VoucherService voucherService;

    public VoucherApiController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/create-voucher")
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher.InputModel inputModel) {
        return ResponseEntity.ok(voucherService.createVoucher(inputModel));
    }

    @GetMapping("/get-voucher-page")
    public ResponseEntity<Page<Voucher>> getVoucherPage(Pageable pageable) {
        return ResponseEntity.ok(voucherService.getVouchersPage(pageable));
    }

    @GetMapping("/get-voucher/{voucherId}")
    public ResponseEntity<Voucher> getVoucher(@PathVariable String voucherId) {
        return ResponseEntity.ok(voucherService.getVoucher(UUID.fromString(voucherId)));
    }

    @GetMapping("/get-voucher-rule-page/{voucherId}")
    public ResponseEntity<Page<VoucherRule.OutputModel>> getVoucherRulePage(
        @PathVariable String voucherId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(voucherService.getVoucherRulesPage(UUID.fromString(voucherId), pageable));
    }

    @PostMapping("/create-voucher-rule/{voucherId}")
    public ResponseEntity<VoucherRule.OutputModel> createVoucherRule(
        @PathVariable String voucherId,
        @RequestBody VoucherRule.InputModel inputModel
    ) {
        return ResponseEntity.ok(voucherService.createVoucherRule(UUID.fromString(voucherId), inputModel));
    }

    @PostMapping("/update-voucher/{voucherId}")
    public ResponseEntity<Voucher> updateVoucher(
        @PathVariable String voucherId,
        @RequestBody Voucher.InputModel inputModel
    ) {
        return ResponseEntity.ok(voucherService.updateVoucher(UUID.fromString(voucherId), inputModel));
    }

    @GetMapping("/delete-voucher/{voucherId}")
    public ResponseEntity<Voucher> deleteVoucher(@PathVariable String voucherId) {
        return ResponseEntity.ok(voucherService.deleteVoucher(UUID.fromString(voucherId)));
    }

    @GetMapping("/get-voucher-rule/{voucherRuleId}")
    public ResponseEntity<VoucherRule.OutputModel> getVoucherRule(@PathVariable String voucherRuleId) {
        return ResponseEntity.ok(voucherService.getVoucherRule(UUID.fromString(voucherRuleId)));
    }

    @PostMapping("/update-voucher-rule/{voucherRuleId}")
    public ResponseEntity<VoucherRule.OutputModel> updateVoucherRule(
        @PathVariable String voucherRuleId,
        @RequestBody VoucherRule.InputModel inputModel
    ) {
        return ResponseEntity.ok(voucherService.updateVoucherRule(UUID.fromString(voucherRuleId), inputModel));
    }

    @GetMapping("/delete-voucher-rule/{voucherRuleId}")
    public ResponseEntity<VoucherRule.OutputModel> deleteVoucherRule(@PathVariable String voucherRuleId) {
        return ResponseEntity.ok(voucherService.deleteVoucherRule(UUID.fromString(voucherRuleId)));
    }
}
