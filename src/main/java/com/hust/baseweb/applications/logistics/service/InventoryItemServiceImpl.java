package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.*;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.repo.InventoryItemRepo;
import com.hust.baseweb.applications.logistics.repo.ProductFacilityRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class InventoryItemServiceImpl implements InventoryItemService {
    public static final String module = InventoryItemServiceImpl.class.getName();

    InventoryItemRepo inventoryItemRepo;
    FacilityService facilityService;
    ProductService productService;
    private ProductFacilityRepo productFacilityRepo;
    private InventoryItemDetailService inventoryItemDetailService;

    @Override
    @Transactional
    public InventoryItem save(ImportInventoryItemInputModel input) {

        System.out.println(module + "::save(" + input.getProductId() + "," + input.getQuantityOnHandTotal() + ")");

        InventoryItem inventoryItem = new InventoryItem();
        Product product = productService.findByProductId(input.getProductId());
        Facility facility = facilityService.findFacilityById(input.getFacilityId());

        if (product == null || facility == null) {
            return null;
        }
        ProductFacility productFacility = productFacilityRepo.findByProductIdAndFacilityId(product.getProductId(), facility.getFacilityId());
        if (productFacility == null) {
            productFacility = new ProductFacility();
            productFacility.setProductId(product.getProductId());
            productFacility.setFacilityId(facility.getFacilityId());
            productFacility.setAtpInventoryCount(new BigDecimal(input.getQuantityOnHandTotal()));
            productFacility.setLastInventoryCount(new BigDecimal(0));
        }

        inventoryItem.setFacility(facility);
        inventoryItem.setProduct(product);
        inventoryItem.setLotId(input.getLotId());
        inventoryItem.setQuantityOnHandTotal(input.getQuantityOnHandTotal());

        productFacility.setLastInventoryCount(productFacility.getLastInventoryCount().add(new BigDecimal(input.getQuantityOnHandTotal())));

        productFacility = productFacilityRepo.save(productFacility);

        return inventoryItemRepo.save(inventoryItem);
    }

    @Override
    @Transactional
    public String exportInventoryItems(ExportInventoryItemsInputModel inventoryItemsInput) {

        List<InventoryItem> inventoryItems = inventoryItemRepo.findAll();// to be improved, find by (productId, facilityId)
        log.info("exportInventoryItems, inventoryItems.sz = " + inventoryItems.size());

        for (int i = 0; i < inventoryItemsInput.getInventoryItems().length; i++) {
            ExportInventoryItemInputModel eii = inventoryItemsInput.getInventoryItems()[i];
            String productId = eii.getProductId();
            String facilityId = eii.getFacilityId();
            int quantity = eii.getQuantity();
            // find list of inventory-items suitable for exporting productId at the facilityId
            //List<InventoryItem> inventoryItems = inventoryItemRepo.findAllByProductIdAndFacilityId(productId, facilityId);

            log.info("exportInventoryItems, productId = " + productId + ", facilityId = " + facilityId + ", list = " + inventoryItems.size());
            List<InventoryItem> selectedInventoryItems = new ArrayList<InventoryItem>();
            BigDecimal totalCount = new BigDecimal(0);// total inventory count of productId in the faicilityId
            for (InventoryItem ii : inventoryItems) {
                if (ii.getQuantityOnHandTotal() > 0 && ii.getProduct().getProductId().equals(productId) && ii.getFacility().getFacilityId().equals(facilityId)) {
                    log.info("exportInventoryItems, productId = " + productId + ", facilityId = " + facilityId + ", qty = " + ii.getQuantityOnHandTotal());
                    selectedInventoryItems.add(ii);
                    totalCount = totalCount.add(new BigDecimal(ii.getQuantityOnHandTotal()));
                }
            }
            InventoryItem[] sortedInventoryItems = new InventoryItem[selectedInventoryItems.size()];
            for (int j = 0; j < selectedInventoryItems.size(); j++) {
                sortedInventoryItems[j] = selectedInventoryItems.get(j);
            }
            // sorting
            for (int j1 = 0; j1 < sortedInventoryItems.length; j1++) {
                for (int j2 = j1 + 1; j2 < sortedInventoryItems.length; j2++) {
                    if (sortedInventoryItems[j1].getQuantityOnHandTotal() > sortedInventoryItems[j2].getQuantityOnHandTotal()) {
                        InventoryItem temp = sortedInventoryItems[j1];
                        sortedInventoryItems[j1] = sortedInventoryItems[j2];
                        sortedInventoryItems[j2] = temp;
                    }
                }
            }

            for (InventoryItem inventoryItem : sortedInventoryItems) {
                if (quantity <= inventoryItem.getQuantityOnHandTotal()) {
                    InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
                    Date effectiveDate = new Date();
                    //iid.setEffectiveDate(effectiveDate);
                    //iid.setInventoryItem(sort_list[j]);
                    //iid.setQuantityOnHandDiff(-qty);
                    inventoryItemDetail = inventoryItemDetailService.save(inventoryItem.getInventoryItemId(), -quantity, effectiveDate);

                    inventoryItem.setQuantityOnHandTotal(inventoryItem.getQuantityOnHandTotal() - quantity);
                    inventoryItemRepo.save(inventoryItem);
                    break;
                } else {
                    InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
                    Date effectiveDate = new Date();
                    //iid.setEffectiveDate(effectiveDate);
                    //iid.setInventoryItem(sort_list[j]);
                    //iid.setQuantityOnHandDiff(-sort_list[j].getQuantityOnHandTotal());
                    inventoryItemDetail = inventoryItemDetailService.save(inventoryItem.getInventoryItemId(), -inventoryItem.getQuantityOnHandTotal(), effectiveDate);

                    inventoryItem.setQuantityOnHandTotal(0);
                    inventoryItemRepo.save(inventoryItem);

                }
            }
            totalCount = totalCount.subtract(new BigDecimal(quantity));// remain total inventory count

            ProductFacility productFacility = productFacilityRepo.findByProductIdAndFacilityId(productId, facilityId);
            if (productFacility == null) {
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
