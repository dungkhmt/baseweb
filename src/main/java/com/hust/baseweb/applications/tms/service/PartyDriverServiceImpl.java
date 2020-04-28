package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.model.DriverModel;
import com.hust.baseweb.applications.tms.repo.PPartyDriverRepo;
import com.hust.baseweb.applications.tms.repo.PartyDriverRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PartyDriverServiceImpl implements PartyDriverService {

    private PPartyDriverRepo pPartyDriverRepo;
    private PartyDriverRepo partyDriverRepo;
    private UserService userService;

    @Override
    @Transactional
    public PartyDriver save(DriverModel.InputCreate input) {
        // TODO:
        PersonModel personModel = new PersonModel();
        personModel.setBirthDate(input.getBirthDate());
        personModel.setFirstName(input.getFirstName());
        personModel.setMiddleName(input.getMiddleName());
        personModel.setLastName(input.getLastName());
        personModel.setUserName(input.getUserName());
        personModel.setPassword(input.getPassword());
        personModel.setPartyCode(input.getPartyCode());
        personModel.setRoles(input.getRoles());
        try {
            Party party = userService.save(personModel);
            PartyDriver partyDriver = new PartyDriver();
            partyDriver.setPartyId(party.getPartyId());
            partyDriver = partyDriverRepo.save(partyDriver);
            return partyDriver;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PartyDriver> findAll() {
        return partyDriverRepo.findAll();
    }

    @Override
    public Page<PartyDriver> findAll(Pageable page) {
        return partyDriverRepo.findAll(page);
    }

    @Override
    public PartyDriver findByPartyId(UUID partyId) {
        return partyDriverRepo.findByPartyId(partyId);
    }

}
