package com.hust.baseweb.applications.sales.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "voucher_id")
    private UUID voucherId;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "min_discount_amount")
    private Double minDiscountAmount;

    @Column(name = "max_discount_amount")
    private Double maxDiscountAmount;

    @Column(name = "min_discount_rate")
    private Double minDiscountRate;

    @Column(name = "max_discount_rate")
    private Double maxDiscountRate;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_limit_per_account")
    private Integer usageLimitPerAccount;

    @Column(name = "usage_count")
    private Integer usageCount;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class InputModel {

        private String code;
        private String description;
        private Date fromDate;
        private Date thruDate;
        private Double minDiscountAmount;
        private Double maxDiscountAmount;
        private Double minDiscountRate;
        private Double maxDiscountRate;
        private Integer usageLimit;
        private Integer usageLimitPerAccount;

        public Voucher toVoucher() {
            return Voucher.builder()
                          .code(code)
                          .description(description)
                          .fromDate(fromDate)
                          .thruDate(thruDate)
                          .minDiscountAmount(minDiscountAmount)
                          .maxDiscountAmount(maxDiscountAmount)
                          .minDiscountRate(minDiscountRate)
                          .maxDiscountRate(maxDiscountRate)
                          .usageLimit(usageLimit)
                          .usageLimitPerAccount(usageLimitPerAccount)
                          .build();
        }

        public void updateToVoucher(Voucher voucher) {
            if (this.getDescription() != null) {
                voucher.setDescription(this.getDescription());
            }
            if (this.getFromDate() != null) {
                voucher.setFromDate(this.getFromDate());
            }
            if (this.getThruDate() != null) {
                voucher.setThruDate(this.getThruDate());
            }
            if (this.getMinDiscountAmount() != null) {
                voucher.setMinDiscountAmount(this.getMinDiscountAmount());
            }
            if (this.getMaxDiscountAmount() != null) {
                voucher.setMaxDiscountAmount(this.getMaxDiscountAmount());
            }
            if (this.getMinDiscountRate() != null) {
                voucher.setMinDiscountRate(this.getMinDiscountRate());
            }
            if (this.getMaxDiscountRate() != null) {
                voucher.setMaxDiscountRate(this.getMaxDiscountRate());
            }
            if (this.getUsageLimit() != null) {
                voucher.setUsageLimit(this.getUsageLimit());
            }
            if (this.getUsageLimitPerAccount() != null) {
                voucher.setUsageLimitPerAccount(this.getUsageLimitPerAccount());
            }
        }
    }
}
