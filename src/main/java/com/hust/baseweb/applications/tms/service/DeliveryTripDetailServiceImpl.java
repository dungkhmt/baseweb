package com.hust.baseweb.applications.tms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;

@Service
public class DeliveryTripDetailServiceImpl implements DeliveryTripDetailService {
	public static Logger LOG = LoggerFactory.getLogger(DeliveryTripDetailServiceImpl.class);
	
	
	@Autowired
	private DeliveryTripDetailRepo deliveryTripDetailRepo;
	
	@Autowired
	private ShipmentItemRepo shipmentItemRepo;
	
	@Override
	public DeliveryTripDetail save(CreateDeliveryTripDetailInputModel input) {
		// TODO Auto-generated method stub
		LOG.info("save, input quantity = " + input.getDeliveryQuantity());
		DeliveryTripDetail deliveryTripDetail = new DeliveryTripDetail();
		deliveryTripDetail.setDeliveryTripId(input.getDeliveryTripId());
		//deliveryTripDetail.setShipmentItem(input.getShipmentId());
		//deliveryTripDetail.setS
		ShipmentItem shipmentItem = shipmentItemRepo.findByShipmentIdAndShipmentItemSeqId(input.getShipmentId(), input.getShipmentItemSeqId());
		
		LOG.info("save, find ShipmentItem " + shipmentItem.getShipmentId() + "," + shipmentItem.getShipmentItemSeqId() + ", product = " + shipmentItem.getProductId());
		
		deliveryTripDetail.setShipmentItem(shipmentItem);
		deliveryTripDetail.setDeliveryQuantity(input.getDeliveryQuantity());
		
		
		deliveryTripDetail = deliveryTripDetailRepo.save(deliveryTripDetail);
		
		return deliveryTripDetail;
	}

}
