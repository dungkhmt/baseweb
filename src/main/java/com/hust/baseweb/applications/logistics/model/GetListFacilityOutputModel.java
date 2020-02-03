package com.hust.baseweb.applications.logistics.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.hust.baseweb.applications.logistics.entity.Facility;
@Getter
@Setter

public class GetListFacilityOutputModel {
	private List<Facility> facilities;

	public GetListFacilityOutputModel(List<Facility> facilities) {
		super();
		this.facilities = facilities;
	}
	
}
