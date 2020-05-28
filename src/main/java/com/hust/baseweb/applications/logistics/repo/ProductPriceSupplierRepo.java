package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPriceSupplier;
import com.hust.baseweb.applications.logistics.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface ProductPriceSupplierRepo extends JpaRepository<ProductPriceSupplier, UUID> {

    List<ProductPriceSupplier> findAllByPartySupplierAndThruDateNullOrThruDateAfter(
        Supplier partySupplier,
        Date thruDateAfter
    );

    List<ProductPriceSupplier> findAllByPartySupplierAndProductAndThruDateNullOrThruDateAfter(
        Supplier partySupplier,
        Product product,
        Date thruDateAfter
    );

    List<ProductPriceSupplier> findAllByPartySupplierInAndProductInAndThruDateNullOrThruDateAfter(
        Collection<Supplier> partySuppliers,
        Collection<Product> products,
        Date thruDateAfter
    );
}
