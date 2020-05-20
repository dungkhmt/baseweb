package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPriceSupplier;
import com.hust.baseweb.applications.logistics.entity.Supplier;
import com.hust.baseweb.applications.logistics.repo.ProductPriceSupplierRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.SupplierRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ProductPriceSupplierServiceImpl implements ProductPriceSupplierService {

    private ProductPriceSupplierRepo productPriceSupplierRepo;
    private SupplierRepo supplierRepo;
    private ProductRepo productRepo;

    @Override
    public List<ProductPriceSupplier.Model> getAllProductPriceSuppliers(String supplierPartyId) {
        Supplier supplier = supplierRepo.findById(UUID.fromString(supplierPartyId))
            .orElseThrow(NoSuchElementException::new);
        Date now = new Date();
        return productPriceSupplierRepo.findAllByPartySupplierAndThruDateNullOrThruDateAfter(supplier, now)
            .stream()
            .map(ProductPriceSupplier::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public ProductPriceSupplier setProductPriceSupplier(ProductPriceSupplier.SetModel setModel) {
        Supplier supplier = supplierRepo.findById(UUID.fromString(setModel.getSupplierPartyId()))
            .orElseThrow(NoSuchElementException::new);
        Product product = productRepo.findById(setModel.getProductId()).orElseThrow(NoSuchElementException::new);
        Date now = new Date();

        List<ProductPriceSupplier> productPriceSupplierHistories = productPriceSupplierRepo
            .findAllByPartySupplierAndProductAndThruDateNullOrThruDateAfter(supplier, product, now);
        productPriceSupplierHistories.forEach(productPriceSupplier -> productPriceSupplier.setThruDate(now));

        ProductPriceSupplier productPriceSupplier = new ProductPriceSupplier(null,
            supplier,
            product,
            setModel.getUnitPrice(),
            now,
            null);
        productPriceSupplierHistories.add(productPriceSupplier);

        productPriceSupplierRepo.saveAll(productPriceSupplierHistories);
        return productPriceSupplier;
    }
}
