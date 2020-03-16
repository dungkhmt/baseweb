package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Uom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UomService {
	public Uom save(String uomId, String uomTypeId, String abbreviation, String description);

	public List<Uom> getAllUoms();

	public Uom getUomByUomId(String uomId);

}
