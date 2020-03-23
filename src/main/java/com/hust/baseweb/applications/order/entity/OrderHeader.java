package com.hust.baseweb.applications.order.entity;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
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

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;


    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    //@OneToMany(fetch = FetchType.LAZY)
    @OneToMany(fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    //@JoinTable(name="OrderRole", inverseJoinColumns=@JoinColumn(name="party_id", referencedColumnName="party_id"),
    //			joinColumns=@JoinColumn(name="order_id", referencedColumnName="order_id"))
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @OneToMany(fetch = FetchType.EAGER)
    private Set<OrderRole> orderRoles;

    public InventoryModel.OrderHeader toOrderHeaderModel(PartyCustomer customer) {
        return new InventoryModel.OrderHeader(
                orderId,
                customer == null ? null : customer.getCustomerCode(),
                orderDate == null ? null : Constant.DATE_FORMAT.format(orderDate)
        );
    }
}
