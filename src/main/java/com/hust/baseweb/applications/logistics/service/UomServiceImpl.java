package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.entity.UomType;
import com.hust.baseweb.applications.logistics.repo.UomRepo;
import com.hust.baseweb.applications.logistics.repo.UomTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UomServiceImpl implements UomService {
    @Autowired
    private UomRepo uomRepo;
    @Autowired
    private UomTypeRepo uomTypeRepo;

    @Override
    public Uom save(String uomId, String uomTypeId, String abbreviation,
                    String description) {
        // TODO Auto-generated method stub
        UomType uomType = uomTypeRepo.findByUomTypeId(uomTypeId);
        Uom uom = new Uom();
        uom.setUomId(uomId);
        uom.setUomType(uomType);
        uom.setAbbreviation(abbreviation);
        uom.setDescription(description);
        uom = uomRepo.save(uom);
        return uom;
    }

}
