package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.entity.UserLogin;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter
//@IdClass(CompositeShipmentItemId.class)
public class ShipmentItem {

    @Id
    @Column(name = "shipment_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID shipmentItemId;

    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id")
    @ManyToOne
    private Shipment shipment;

    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "pallet")
    private Double pallet;

    @Column(name = "order_id", insertable = false, updatable = false)
    private String orderId;

    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @JoinColumn(name = "order_item_seq_id", referencedColumnName = "order_item_seq_id")
    @OneToOne(fetch = FetchType.LAZY)
    private OrderItem orderItem;

    //@JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    //@ManyToOne(fetch = FetchType.LAZY)
    //private PartyCustomer customer;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Party partyCustomer;

    @JoinColumn(name = "ship_to_location_id", referencedColumnName = "contact_mech_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostalAddress shipToLocation;

    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StatusItem statusItem;

    @JoinColumn(name = "processed_by_user_login_id", referencedColumnName = "user_login_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserLogin userLogin;

    private Integer scheduledQuantity = 0;
    private Integer completedQuantity = 0;

    public ShipmentItemModel toShipmentItemModel() {
        String customerCode = null;
        String locationCode = null;
        String address = null;
        Double lat = null;
        Double lng = null;
        //if (customer != null) {
        //    customerCode = customer.getCustomerCode();
        //}
        if (partyCustomer != null) {
            customerCode = partyCustomer.getPartyCode();
        }
        if (shipToLocation != null) {
            locationCode = shipToLocation.getLocationCode();
            address = shipToLocation.getAddress();
            if (shipToLocation.getGeoPoint() != null) {
                GeoPoint geoPoint = shipToLocation.getGeoPoint();
                lat = geoPoint.getLatitude();
                lng = geoPoint.getLongitude();
            }
        }

        return new ShipmentItemModel(
            shipmentItemId.toString(),
            quantity,
            pallet,
            orderItem.getProduct().getProductId(),
            customerCode,
            locationCode,
            address,
            lat + "",
            lng + "",
            orderItem.getProduct().getWeight() * quantity,
            scheduledQuantity,
            completedQuantity,
            Optional.ofNullable(facility).map(Facility::getFacilityId).orElse(null),
            Optional.ofNullable(statusItem).map(StatusItem::getStatusId).orElse(null)
        );
    }

    public ShipmentItemModel.DeliveryPlan toShipmentItemDeliveryPlanModel(int assignedQuantity) {
        return new ShipmentItemModel.DeliveryPlan(
            shipmentItemId,
            orderItem.getProduct().getProductName(),
            orderItem.getProduct().getWeight(),
            quantity - assignedQuantity,
            pallet,
            shipToLocation.getAddress(),
            shipToLocation.getGeoPoint().getLatitude() + "," +
            shipToLocation.getGeoPoint().getLongitude()
        );
    }
}
