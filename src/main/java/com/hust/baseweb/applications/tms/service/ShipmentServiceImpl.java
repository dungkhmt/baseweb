package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentItemInputModel;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentRepo;
import com.hust.baseweb.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ShipmentServiceImpl implements ShipmentService {
    private ShipmentRepo shipmentRepo;
    private ShipmentItemRepo shipmentItemRepo;

    @Override
    public Shipment save(CreateShipmentInputModel input) {

        UUID shipmentId = UUID.randomUUID();
        Shipment shipment = new Shipment();
        shipment.setShipmentId(shipmentId);
        shipment.setShipmentTypeId("SALES_SHIPMENT");
        shipmentRepo.save(shipment);

        int idx = 0;
        List<ShipmentItem> shipmentItems = new ArrayList<>();
        for (int i = 0; i < input.getShipmentItems().length; i++) {
            CreateShipmentItemInputModel shipmentItemModel = input.getShipmentItems()[i];
            log.info("::save, idx = " + idx + ", product = " + shipmentItemModel.getProductId() + ", quantity = " + shipmentItemModel.getQuantity() + " pallet = " + shipmentItemModel.getAmountPallet());

            idx++;
            String shipmentItemSeqId = CommonUtils.buildSeqId(idx);
            ShipmentItem shipmentItem = new ShipmentItem();
            shipmentItem.setShipmentId(shipmentId);
            shipmentItem.setShipmentItemSeqId(shipmentItemSeqId);
            shipmentItem.setQuantity(shipmentItemModel.getQuantity());
            shipmentItem.setPallet(shipmentItemModel.getAmountPallet());
            shipmentItem.setProductId(shipmentItemModel.getProductId());
            shipmentItemRepo.save(shipmentItem);

            shipmentItems.add(shipmentItem);
        }

        shipment.setShipmentItems(shipmentItems);
        return shipment;
    }

}
