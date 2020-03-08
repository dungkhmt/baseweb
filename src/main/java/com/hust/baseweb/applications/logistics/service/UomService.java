package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Uom;
import org.springframework.stereotype.Service;

@Service
public interface UomService {
    public Uom save(String uomId, String uomTypeId, String abbreviation, String description);
}
