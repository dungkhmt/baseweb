package com.hust.baseweb.applications.logistics.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.repo.InventoryItemRepo;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {
	public static final String module = InventoryItemServiceImpl.class.getName();
	@Autowired
	InventoryItemRepo inventoryItemRepo;
	
	@Autowired
	FacilityService facilityService;
	
	@Autowired
	ProductService productService;
	
	@Override
	@Transactional
	public InventoryItem save(ImportInventoryItemInputModel input) {
		// TODO Auto-generated method stub
		System.out.println(module + "::save(" + input.getProductId() + "," + input.getQuantityOnHandTotal() + ")");
		
		InventoryItem ii = new InventoryItem();
		Product product = productService.findByProductId(input.getProductId());
		Facility facility = facilityService.findFacilityById(input.getFacilityId());
		if(product == null || facility == null){
			return null;
		}
		ii.setFacility(facility);
		ii.setProduct(product);
		ii.setLotId(input.getLotId());
		ii.setQuantityOnHandTotal(input.getQuantityOnHandTotal());
		return inventoryItemRepo.save(ii);
	}

}
