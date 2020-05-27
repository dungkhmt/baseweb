package com.hust.baseweb.applications.order.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompositeOrderItemId implements Serializable {

    private String orderId;
    private String orderItemSeqId;
}
