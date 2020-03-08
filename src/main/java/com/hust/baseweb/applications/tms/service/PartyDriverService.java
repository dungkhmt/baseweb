package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.model.driver.CreateDriverInputModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PartyDriverService {
    PartyDriver save(CreateDriverInputModel input);

    List<PartyDriver> findAll();
}
