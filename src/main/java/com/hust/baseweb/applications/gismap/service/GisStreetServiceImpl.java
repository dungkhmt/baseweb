package com.hust.baseweb.applications.gismap.service;

import com.hust.baseweb.applications.gismap.document.Street;
import com.hust.baseweb.applications.gismap.repo.StreetRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class GisStreetServiceImpl implements GisMapService {

    private StreetRepo streetRepo;

    @Override
    public Street save(Street street) {
        street = streetRepo.save(street);
        return street;
    }

    @Override
    public List<Street> findAll() {
        return streetRepo.findAll();
    }

    @Override
    public Street removeStreet(String streetId) {
        Street street = streetRepo.findByStreetId(streetId);
        streetRepo.deleteByStreetId(streetId);
        return street;
    }
}
