package com.hust.baseweb.applications.sales.entity;

import com.hust.baseweb.utils.Constant;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */

@Getter
@Setter
@Entity(name = "voucher_rule")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRule {

    @Id
    @Column(name = "voucher_constraint_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID voucherConstraintId;

    @JoinColumn(name = "voucher_id", referencedColumnName = "voucher_id")
    @ManyToOne()
    private Voucher voucher;

    @Column(name = "type")
    private String type;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_category_id")
    private String productCategoryId;

    @Column(name = "product_transport_category_id")
    private String productTransportCategoryId;

    @Column(name = "min_order_value")
    private Double minOrderValue;

    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "vendor_category_id")
    private String vendorCategoryId;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "customer_category_id")
    private String customerCategoryId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "created_date")
    private Date createdDate;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class InputModel {

        private UUID voucherId;
        private String type;
        private String productId;
        private String productCategoryId;
        private String productTransportCategoryId;
        private Double minOrderValue;
        private String vendorCode;
        private String vendorCategoryId;
        private String customerCode;
        private String customerCategoryId;
        private String paymentMethod;

        public VoucherRule toVoucherRule(Function<UUID, Voucher> voucherFunction) {
            return VoucherRule.builder()
                              .voucher(voucherFunction.apply(voucherId))
                              .type(type)
                              .productId(productId)
                              .productCategoryId(productCategoryId)
                              .productTransportCategoryId(productTransportCategoryId)
                              .minOrderValue(minOrderValue)
                              .vendorCode(vendorCode)
                              .vendorCategoryId(vendorCategoryId)
                              .customerCode(customerCode)
                              .customerCategoryId(customerCategoryId)
                              .paymentMethod(paymentMethod)
                              .build();
        }

        public void updateToVoucherRule(VoucherRule voucherRule) {
            if (this.getType() != null) {
                voucherRule.setType(this.getType());
            }
            if (this.getProductId() != null) {
                voucherRule.setProductId(this.getProductId());
            }
            if (this.getProductCategoryId() != null) {
                voucherRule.setProductCategoryId(this.getProductCategoryId());
            }
            if (this.getProductTransportCategoryId() != null) {
                voucherRule.setProductTransportCategoryId(this.getProductTransportCategoryId());
            }
            if (this.getMinOrderValue() != null) {
                voucherRule.setMinOrderValue(this.getMinOrderValue());
            }
            if (this.getVendorCode() != null) {
                voucherRule.setVendorCode(this.getVendorCode());
            }
            if (this.getVendorCategoryId() != null) {
                voucherRule.setVendorCategoryId(this.getVendorCategoryId());
            }
            if (this.getCustomerCode() != null) {
                voucherRule.setCustomerCode(this.getCustomerCode());
            }
            if (this.getCustomerCategoryId() != null) {
                voucherRule.setCustomerCategoryId(this.getCustomerCategoryId());
            }
            if (this.getPaymentMethod() != null) {
                voucherRule.setPaymentMethod(this.getPaymentMethod());
            }
        }
    }

    @Getter
    @Setter
    @Builder
    public static class OutputModel {

        private UUID voucherConstraintId;
        private UUID voucherId;
        private String type;
        private String productId;
        private String productCategoryId;
        private String productTransportCategoryId;
        private Double minOrderValue;
        private String vendorCode;
        private String vendorCategoryId;
        private String customerCode;
        private String customerCategoryId;
        private String paymentMethod;
        private String createdDate;
    }

    public OutputModel toOutputModel() {
        return OutputModel.builder()
                          .voucherConstraintId(voucherConstraintId)
                          .voucherId(voucher.getVoucherId())
                          .type(type)
                          .productId(productId)
                          .productCategoryId(productCategoryId)
                          .productTransportCategoryId(productTransportCategoryId)
                          .minOrderValue(minOrderValue)
                          .vendorCode(vendorCode)
                          .vendorCategoryId(vendorCategoryId)
                          .customerCode(customerCode)
                          .customerCategoryId(customerCategoryId)
                          .paymentMethod(paymentMethod)
                          .createdDate(Constant.DATE_FORMAT.format(createdDate))
                          .build();
    }
}
