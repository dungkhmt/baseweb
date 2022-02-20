package com.hust.baseweb.applications.order.document;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderHeaderRemoved {

    @MongoId
    private ObjectId orderHeaderRemovedId;

    private String orderId;

    private String orderType;

    private String salesChannelId;

    private Date orderDate;

    private Double grandTotal;

    private UUID contactMechId;

    private Boolean exported = false;

    private Date createdStamp;

    private Date lastUpdatedStamp;

    private UUID partyCustomerId;

    private UUID partyVendorId;

    private UUID partySalesmanId;

    private String saleManId;

    private List<OrderItemRemoved> orderItems;

    private List<OrderRoleRemoved> orderRoles;

    private List<OrderStatusRemoved> orderStatuses;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderItemRemoved {

        private String orderId;

        private String orderItemSeqId;

        private String productId;

        private Double unitPrice;

        private Integer quantity;

        private Integer exportedQuantity = 0;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderRoleRemoved {

        private String orderId;

        private UUID partyId;

        private String roleTypeId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderStatusRemoved {

        private String orderStatusId;

        private String statusId;
    }

    public static void main(String[] args) {
//        OrderRole orderRole = new OrderRole();
//        orderRole.setOrderId("1");
//        orderRole.setPartyId(UUID.randomUUID());
//        orderRole.setRoleTypeId("2");
//
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        OrderRoleRemoved orderRoleRemoved = modelMapper.map(orderRole, OrderRoleRemoved.class);
//        System.out.println(orderRoleRemoved);

        Product product = new Product();
        product.setProductId("prporpropr");
//        OrderItem orderItem = new OrderItem("1", "2", product, 1.0, 2, 3);
        OrderItem orderItem = new OrderItem();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        OrderItemRemoved orderItemRemoved = modelMapper.map(orderItem, OrderItemRemoved.class);
        System.out.println(orderItemRemoved);

    }
}
