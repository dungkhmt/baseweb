package com.hust.baseweb.applications.gismap.service;


import com.hust.baseweb.applications.gismap.document.Street;

import java.util.List;

public interface GisMapService {
    public Street save(Street street);
    public List<Street> findAll();
    public Street removeStreet(String streetId);
}
