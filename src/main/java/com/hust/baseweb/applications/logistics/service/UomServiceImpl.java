package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.entity.UomType;
import com.hust.baseweb.applications.logistics.repo.UomRepo;
import com.hust.baseweb.applications.logistics.repo.UomTypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UomServiceImpl implements UomService {

    private UomRepo uomRepo;
    private UomTypeRepo uomTypeRepo;

    @Override
    public Uom save(
        String uomId, String uomTypeId, String abbreviation,
        String description
    ) {
        UomType uomType = uomTypeRepo.findByUomTypeId(uomTypeId);
        Uom uom = new Uom();
        uom.setUomId(uomId);
        uom.setUomType(uomType);
        uom.setAbbreviation(abbreviation);
        uom.setDescription(description);
        uom = uomRepo.save(uom);
        return uom;
    }

    @Override
    public List<Uom> getAllUoms() {
        return uomRepo.findAll();
    }

    @Override
    public Uom getUomByUomId(String uomId) {
        return uomRepo.findByUomId(uomId);
    }
}
