package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Uom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UomService {

    Uom save(String uomId, String uomTypeId, String abbreviation, String description);

    List<Uom> getAllUoms();

    Uom getUomByUomId(String uomId);

}
