package com.hust.baseweb.applications.tms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentItemInputModel;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentRepo;
import com.hust.baseweb.utils.CommonUtils;

@Service
public class ShipmentServiceImpl implements ShipmentService {
	public static Logger LOG = LoggerFactory.getLogger(ShipmentServiceImpl.class);
	
	@Autowired
	private ShipmentRepo shipmentRepo;
	
	@Autowired
	private ShipmentItemRepo shipmentItemRepo;
	
	@Override
	public Shipment save(CreateShipmentInputModel input) {
		// TODO Auto-generated method stub
		UUID shipmentId = UUID.randomUUID();
		Shipment shipment = new Shipment();
		shipment.setShipmentId(shipmentId);
		shipment.setShipmentTypeId("SALES_SHIPMENT");		
		shipmentRepo.save(shipment);
		
		int idx = 0;
		List<ShipmentItem> shipmentItems = new ArrayList<ShipmentItem>();
		for(int i = 0; i < input.getShipmentItems().length; i++){
			CreateShipmentItemInputModel sii = input.getShipmentItems()[i];
			LOG.info("::save, idx = " + idx + ", product = " + sii.getProductId() + ", quantity = " + sii.getQuantity() + " pallet = " + sii.getAmountPallet());
			
			idx++;
			String shipmentItemSeqId = CommonUtils.buildSeqId(idx);
			ShipmentItem si = new ShipmentItem();
			si.setShipmentId(shipmentId);
			si.setShipmentItemSeqId(shipmentItemSeqId);
			si.setQuantity(sii.getQuantity() );
			si.setPallet(sii.getAmountPallet());
			si.setProductId(sii.getProductId());
			shipmentItemRepo.save(si);
			
			shipmentItems.add(si);
		}
		
		shipment.setShipmentItems(shipmentItems);
		return shipment;
	}

}
