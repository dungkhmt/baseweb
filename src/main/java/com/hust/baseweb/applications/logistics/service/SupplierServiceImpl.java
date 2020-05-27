package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Supplier;
import com.hust.baseweb.applications.logistics.repo.SupplierRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.Status;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import com.hust.baseweb.repo.StatusRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepo supplierRepo;
    private PartyRepo partyRepo;
    private PartyTypeRepo partyTypeRepo;
    private StatusRepo statusRepo;

    @Override
    public Supplier getSupplierById(String supplierPartyId) {

        return supplierRepo.findById(UUID.fromString(supplierPartyId)).orElse(null);
    }

    @Override
    public List<Supplier> getAllSupplier() {

        return supplierRepo.findAll();
    }

    @Override
    public Supplier create(Supplier.CreateModel supplierModel) {

        Party party = new Party(null, partyTypeRepo.findByPartyTypeId("PARTY_SUPPLIER"), "",
                                statusRepo
                                    .findById(Status.StatusEnum.PARTY_ENABLED.name())
                                    .orElseThrow(NoSuchElementException::new),
                                false);
        party = partyRepo.save(party);
        return supplierRepo.save(new Supplier(
            party.getPartyId(),
            supplierModel.getSupplierName(),
            supplierModel.getSupplierCode()));
    }
}
