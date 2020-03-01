package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class ModelCreateOrderInput {
    private UUID toCustomerId;// party_id
    private UUID fromVendorId;// party_id
    private ModelCreateOrderInputOrderItem[] orderItems;
    private String salesChannelId;
    private String facilityId;
    private String salesmanId;// user_login_id
    private String orderDate;
    private UUID shipToAddressId;
    private String shipToAddress;
    
    public ModelCreateOrderInput() {
        super();

    }

}
