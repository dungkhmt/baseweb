package com.hust.baseweb.applications.tms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */

public class DeliveryPlanModel {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {

        private String deliveryDate;
        private String createdByUserLoginId;
        private String facilityId;
        private String description;
    }
}
