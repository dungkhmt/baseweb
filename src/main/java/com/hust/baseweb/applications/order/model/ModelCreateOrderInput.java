package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class ModelCreateOrderInput {
    private UUID partyCustomerId;
    private ModelCreateOrderInputOrderItem[] orderItems;
    private String salesChannelId;
    private String facilityId;
    private String salesmanId;
    private String orderDate;

    public ModelCreateOrderInput() {
        super();

    }

}
