package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class DeliveryTripDetailServiceImpl implements DeliveryTripDetailService {

    private DeliveryTripDetailRepo deliveryTripDetailRepo;
    private ShipmentItemRepo shipmentItemRepo;

    @Override
    public DeliveryTripDetail save(CreateDeliveryTripDetailInputModel input) {
        log.info("save, input quantity = " + input.getDeliveryQuantity());
        DeliveryTripDetail deliveryTripDetail = new DeliveryTripDetail();
        deliveryTripDetail.setDeliveryTripId(input.getDeliveryTripId());
        //deliveryTripDetail.setShipmentItem(input.getShipmentId());
        //deliveryTripDetail.setS
        ShipmentItem shipmentItem = shipmentItemRepo.findByShipmentIdAndShipmentItemSeqId(input.getShipmentId(), input.getShipmentItemSeqId());

        log.info("save, find ShipmentItem " + shipmentItem.getShipmentId() + "," + shipmentItem.getShipmentItemSeqId() + ", product = " + shipmentItem.getProductId());

        deliveryTripDetail.setShipmentItem(shipmentItem);
        deliveryTripDetail.setDeliveryQuantity(input.getDeliveryQuantity());


        deliveryTripDetail = deliveryTripDetailRepo.save(deliveryTripDetail);

        return deliveryTripDetail;
    }

}
