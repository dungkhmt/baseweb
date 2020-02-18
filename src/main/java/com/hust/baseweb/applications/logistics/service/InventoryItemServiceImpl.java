package com.hust.baseweb.applications.logistics.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductFacility;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.repo.InventoryItemRepo;
import com.hust.baseweb.applications.logistics.repo.ProductFacilityRepo;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {
	public static final String module = InventoryItemServiceImpl.class.getName();
	public static Logger LOG = LoggerFactory.getLogger(InventoryItemServiceImpl.class);
	
	@Autowired
	InventoryItemRepo inventoryItemRepo;
	
	@Autowired
	FacilityService facilityService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private ProductFacilityRepo productFacilityRepo;
	
	@Autowired
	private InventoryItemDetailService inventoryItemDetailService;
	
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
		ProductFacility productFacility = productFacilityRepo.findByProductIdAndFacilityId(product.getProductId(), facility.getFacilityId());
		if(productFacility == null){
			productFacility = new ProductFacility();
			productFacility.setProductId(product.getProductId());
			productFacility.setFacilityId(facility.getFacilityId());
			productFacility.setAtpInventoryCount(new BigDecimal(input.getQuantityOnHandTotal()));
			productFacility.setLastInventoryCount(new BigDecimal(0));
		}
		
		ii.setFacility(facility);
		ii.setProduct(product);
		ii.setLotId(input.getLotId());
		ii.setQuantityOnHandTotal(input.getQuantityOnHandTotal());
		
		productFacility.setLastInventoryCount(productFacility.getLastInventoryCount().add(new BigDecimal(input.getQuantityOnHandTotal())));
		
		productFacility = productFacilityRepo.save(productFacility);
		
		return inventoryItemRepo.save(ii);
	}

	@Override
	@Transactional
	public String exportInventoryItems(ExportInventoryItemsInputModel inventoryItemsInput){
		
		List<InventoryItem> inventoryItems = inventoryItemRepo.findAll();// to be improved, find by (productId, facilityId)
		LOG.info("exportInventoryItems, inventoryItems.sz = " + inventoryItems.size());
		
		for(int i = 0; i < inventoryItemsInput.getInventoryItems().length; i++){
			ExportInventoryItemInputModel eii = inventoryItemsInput.getInventoryItems()[i];
			String productId = eii.getProductId();
			String facilityId = eii.getFacilityId();
			int qty = eii.getQuantity();
			// find list of inventory-items suitable for exporting productId at the facilityId
			//List<InventoryItem> inventoryItems = inventoryItemRepo.findAllByProductIdAndFacilityId(productId, facilityId);
			
			LOG.info("exportInventoryItems, productId = " + productId + ", facilityId = " + facilityId + ", list = " + inventoryItems.size());
			List<InventoryItem> sel_ii = new ArrayList<InventoryItem>();
			BigDecimal totalCount = new BigDecimal(0);// total inventory count of productId in the faicilityId
			for(InventoryItem ii: inventoryItems){
				if(ii.getQuantityOnHandTotal() > 0 && ii.getProduct().getProductId().equals(productId) && ii.getFacility().getFacilityId().equals(facilityId)){
					LOG.info("exportInventoryItems, productId = " + productId + ", facilityId = " + facilityId + ", qty = " + ii.getQuantityOnHandTotal());
					sel_ii.add(ii);
					totalCount = totalCount.add(new BigDecimal(ii.getQuantityOnHandTotal()));
				}
			}
			InventoryItem[] sort_list = new InventoryItem[sel_ii.size()];
			for(int j = 0; j < sel_ii.size(); j++){
				sort_list[j] = sel_ii.get(j);
			}
			// sorting
			for(int j1 = 0; j1 < sort_list.length; j1++){
				for(int j2 = j1+1; j2 < sort_list.length; j2++){
					if(sort_list[j1].getQuantityOnHandTotal() > sort_list[j2].getQuantityOnHandTotal()){
						InventoryItem tmp = sort_list[j1]; 
						sort_list[j1] = sort_list[j2];
						sort_list[j2] = tmp;
					}
				}
			}
			
			for(int j = 0; j < sort_list.length; j++){
				if(qty <= sort_list[j].getQuantityOnHandTotal()){
					InventoryItemDetail iid = new InventoryItemDetail();
					Date effectiveDate = new Date();
					//iid.setEffectiveDate(effectiveDate);
					//iid.setInventoryItem(sort_list[j]);
					//iid.setQuantityOnHandDiff(-qty);
					iid = inventoryItemDetailService.save(sort_list[j].getInventoryItemId(),-qty,effectiveDate);
					
					sort_list[j].setQuantityOnHandTotal(sort_list[j].getQuantityOnHandTotal()-qty);
					inventoryItemRepo.save(sort_list[j]);
					break;
				}else{
					InventoryItemDetail iid = new InventoryItemDetail();
					Date effectiveDate = new Date();
					//iid.setEffectiveDate(effectiveDate);
					//iid.setInventoryItem(sort_list[j]);
					//iid.setQuantityOnHandDiff(-sort_list[j].getQuantityOnHandTotal());
					iid = inventoryItemDetailService.save(sort_list[j].getInventoryItemId(),-sort_list[j].getQuantityOnHandTotal(),effectiveDate);
					
					sort_list[j].setQuantityOnHandTotal(0);
					inventoryItemRepo.save(sort_list[j]);
					
				}
			}
			totalCount = totalCount.subtract(new BigDecimal(qty));// remain total inventory count
			
			ProductFacility productFacility = productFacilityRepo.findByProductIdAndFacilityId(productId, facilityId);
			if(productFacility == null){
				productFacility = new ProductFacility();
				productFacility.setProductId(productId);
				productFacility.setFacilityId(facilityId);
				productFacility.setAtpInventoryCount(totalCount);
			}
			productFacility.setLastInventoryCount(totalCount);
			productFacility = productFacilityRepo.save(productFacility);
			
		}
		return "ok";
	}
}
