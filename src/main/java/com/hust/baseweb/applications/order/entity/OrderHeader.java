package com.hust.baseweb.applications.order.entity;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHeader {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private String orderId;

    @JoinColumn(name = "order_type_id", referencedColumnName = "order_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderType orderType;

    @JoinColumn(name = "sales_channel_id", referencedColumnName = "sales_channel_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesChannel salesChannel;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "grand_total")
    private Double grandTotal;

    @JoinColumn(name = "ship_to_address_id", referencedColumnName = "contact_mech_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PostalAddress shipToPostalAddress;

    private Boolean exported = false;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    //private PartyCustomer partyCustomer;
    private Party partyCustomer;

    @JoinColumn(name = "vendor_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Party partyVendor;

    @JoinColumn(name = "party_salesman_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Party partySalesman;


    //private UUID vendorId;

    private String saleManId;

    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    //@OneToMany(fetch = FetchType.LAZY)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    //@JoinTable(name="OrderRole", inverseJoinColumns=@JoinColumn(name="party_id", referencedColumnName="party_id"),
    //			joinColumns=@JoinColumn(name="order_id", referencedColumnName="order_id"))
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @OneToMany(fetch = FetchType.EAGER)
    private Set<OrderRole> orderRoles;

    @Transient
    private List<OrderStatus> orderStatuses;

    @NotNull
    public static String convertSequenceIdToOrderId(Long id) {
        return "ORD" + String.format("%010d", id);
    }

    public InventoryModel.OrderHeader toOrderHeaderModel() {
        return new InventoryModel.OrderHeader(
            orderId,
            partyCustomer.getPartyId().toString(),
            Optional.ofNullable(orderDate).map(date -> Constant.DATE_FORMAT.format(orderDate)).orElse(null)
        );
    }

    public PurchaseModel toPurchaseModel() {
        return toPurchaseModel(null, null);
    }

    public PurchaseModel toPurchaseModel(String supplierName, Double totalAmount) {
        return new PurchaseModel(
            orderId,
            Constant.DATE_FORMAT.format(createdStamp),
            supplierName,
            totalAmount
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PurchaseModel {

        private String orderId;
        private String createdStamp;
        private String supplierName;
        private Double totalAmount;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PurchaseCreateModel {

        private String supplierPartyId;
        private List<ProductQuantity> productQuantities;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ProductQuantity {

            private String productId;
            private Integer quantity;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DeleteModel {

        private String fromDate;
        private String toDate;
    }
}
