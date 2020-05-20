package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.*;
import com.hust.baseweb.applications.logistics.repo.ReceiptItemRepo;
import com.hust.baseweb.applications.logistics.repo.ReceiptRepo;
import com.hust.baseweb.applications.logistics.repo.ReceiptSequenceIdRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

    private ReceiptRepo receiptRepo;
    private ReceiptSequenceIdRepo receiptSequenceIdRepo;
    private ReceiptItemRepo receiptItemRepo;

    @Override
    public Receipt create(Facility facility) {
        ReceiptSequenceId id = receiptSequenceIdRepo.save(new ReceiptSequenceId());
        return receiptRepo.save(new Receipt(Receipt.convertSequenceIdToReceiptId(id.getId()), new Date(), facility));
    }

    @Override
    public List<ReceiptItem> createReceiptItems(Receipt receipt,
                                                List<InventoryItem> inventoryItems) {
        List<ReceiptItem> receiptItems = inventoryItems.stream()
            .map(inventoryItem -> new ReceiptItem(null,
                receipt,
                inventoryItem.getProduct(),
                inventoryItem.getQuantityOnHandTotal()))
            .collect(Collectors.toList());
        return receiptItemRepo.saveAll(receiptItems);
    }
}
