package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface SupplierRepo extends JpaRepository<Supplier, UUID> {

    List<Supplier> findAllByPartyIdIn(Collection<UUID> partyIds);
}
