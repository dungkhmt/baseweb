package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Supplier;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface SupplierService {

    Supplier getSupplierById(String supplierPartyId);

    List<Supplier> getAllSupplier();

    Supplier create(Supplier.CreateModel supplierModel);
}
