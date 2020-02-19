package com.hust.baseweb.applications.logistics.model;

import com.hust.baseweb.applications.logistics.entity.Facility;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetListFacilityOutputModel {
    private List<Facility> facilities;

    public GetListFacilityOutputModel(List<Facility> facilities) {
        super();
        this.facilities = facilities;
    }

}
