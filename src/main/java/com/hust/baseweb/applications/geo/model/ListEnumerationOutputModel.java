package com.hust.baseweb.applications.geo.model;

import com.hust.baseweb.applications.geo.entity.Enumeration;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEnumerationOutputModel {

    List<Enumeration> enumerationList;

    public ListEnumerationOutputModel(List<Enumeration> enumerationList) {

        this.enumerationList = enumerationList;
    }
}
