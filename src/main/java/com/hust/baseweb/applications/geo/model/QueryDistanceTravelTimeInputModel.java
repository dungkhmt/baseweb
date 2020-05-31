package com.hust.baseweb.applications.geo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryDistanceTravelTimeInputModel {

    private String params;
    private List<DistanceTravelTimeElement> elements;
}
