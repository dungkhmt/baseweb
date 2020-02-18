package com.hust.baseweb.applications.logistics.service;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import com.hust.baseweb.applications.logistics.repo.InventoryItemDetailRepo;
import com.hust.baseweb.applications.logistics.repo.InventoryItemRepo;

@Service
public class InventoryItemDetailServiceImpl implements
		InventoryItemDetailService {
	public static Logger LOG = LoggerFactory.getLogger(InventoryItemDetailServiceImpl.class);
	@Autowired
	private InventoryItemDetailRepo inventoryItemDetailRepo;
	@Autowired
	private InventoryItemRepo inventoryItemRepo;
	
	@Override
	@Transactional
	public InventoryItemDetail save(UUID inventoryItemId, int qtyOnHandDiff,
			Date effectiveDate) {
		LOG.info("save, inventoryItemId = " + inventoryItemId + ", qtyOnHandDiff = " + qtyOnHandDiff + ", effectiveDate = " + effectiveDate.toString());
		// TODO Auto-generated method stub
		InventoryItemDetail iid = new InventoryItemDetail();
		iid.setEffectiveDate(effectiveDate);
		InventoryItem inventoryItem = inventoryItemRepo.findByInventoryItemId(inventoryItemId);
		iid.setInventoryItem(inventoryItem);
		iid.setQuantityOnHandDiff(qtyOnHandDiff);
		
		iid = inventoryItemDetailRepo.save(iid);
		return iid;
	}

}
