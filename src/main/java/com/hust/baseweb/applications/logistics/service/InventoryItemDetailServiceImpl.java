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
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class InventoryItemDetailServiceImpl implements
        InventoryItemDetailService {
    private InventoryItemDetailRepo inventoryItemDetailRepo;
    private InventoryItemRepo inventoryItemRepo;

    @Override
    @Transactional
    public InventoryItemDetail save(UUID inventoryItemId, int qtyOnHandDiff,
                                    Date effectiveDate) {
        log.info("save, inventoryItemId = " + inventoryItemId + ", qtyOnHandDiff = " + qtyOnHandDiff + ", effectiveDate = " + effectiveDate.toString());

        InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
        inventoryItemDetail.setEffectiveDate(effectiveDate);
        InventoryItem inventoryItem = inventoryItemRepo.findByInventoryItemId(inventoryItemId);
        inventoryItemDetail.setInventoryItem(inventoryItem);
        inventoryItemDetail.setQuantityOnHandDiff(qtyOnHandDiff);

        inventoryItemDetail = inventoryItemDetailRepo.save(inventoryItemDetail);
        return inventoryItemDetail;
    }

}
