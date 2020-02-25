package com.hust.baseweb.applications.logistics.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.Uom;
@Service
public interface UomService {
	public Uom save(String uomId, String uomTypeId, String abbreviation, String description);
}
