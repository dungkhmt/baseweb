package com.hust.baseweb.applications.logistics.model;

import com.hust.baseweb.applications.logistics.entity.Uom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListUomOutputModel {
    private List<Uom> uoms;

    public GetListUomOutputModel(List<Uom> uoms) {
        super();
        this.uoms = uoms;
    }


}
