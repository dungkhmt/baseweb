package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import com.hust.baseweb.applications.logistics.repo.InventoryItemDetailRepo;
import com.hust.baseweb.applications.logistics.repo.InventoryItemRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class InventoryItemDetailServiceImpl implements InventoryItemDetailService {
    private InventoryItemDetailRepo inventoryItemDetailRepo;
    private InventoryItemRepo inventoryItemRepo;

    @Override
    @Transactional
    public InventoryItemDetail save(InventoryItem inventoryItem, int qtyOnHandDiff) {
        Date effectiveDate = new Date();
        log.info("save, inventoryItemId = " +
                inventoryItem + ", qtyOnHandDiff = " + qtyOnHandDiff + ", effectiveDate = " + effectiveDate.toString());

        InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
        inventoryItemDetail.setEffectiveDate(effectiveDate);
        inventoryItemDetail.setInventoryItem(inventoryItem);
        inventoryItemDetail.setQuantityOnHandDiff(qtyOnHandDiff);

        inventoryItemDetail = inventoryItemDetailRepo.save(inventoryItemDetail);
        return inventoryItemDetail;
    }

}
