package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PersonRepo;
import com.hust.baseweb.repo.UserLoginRepo;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PartySalesmanServiceImpl implements PartySalesmanService {

    private PartySalesmanRepo partySalesmanRepo;
    private UserLoginRepo userLoginRepo;
    private UserService userService;
    private PartyRepo partyRepo;
    private PersonRepo personRepo;

    @Override
    public List<GetSalesmanOutputModel> findAllSalesman() {
        // TODO: *** should be improved by defining entity PartySalesman
        List<PartySalesman> salesman = partySalesmanRepo.findAll();
        List<GetSalesmanOutputModel> retList = new ArrayList<GetSalesmanOutputModel>();
        for (PartySalesman sm : salesman) {
            UUID partyId = sm.getPartyId();
            //Party party = partyRepo.findByPartyId(partyId);
            UserLogin userLogin = null;

            // TODO: to be improved by defining entity and relation
            List<UserLogin> userLogins = userLoginRepo.findAll();
            for (UserLogin u : userLogins) {
                if (u.getParty().getPartyId().equals(partyId)) {
                    userLogin = u;
                    break;
                }
            }
            //List<UserLogin> ul = userLoginRepo.findByParty(party);
            //if(ul != null && ul.size() > 0)
            //	u = ul.get(0);
            log.info("findAllSalesman, GOT user-login " + (userLogin != null ? userLogin.getUserLoginId() : " NULL"));
            GetSalesmanOutputModel smm = new GetSalesmanOutputModel();
            smm.setPartySalesman(sm);
            smm.setUserLoginId(userLogin.getUserLoginId());
            retList.add(smm);
        }
        return retList;
    }

    @Override
    public UserLogin findUserLoginOfSalesmanId(UUID partySalesmanId) {

        Party party = partyRepo.findByPartyId(partySalesmanId);
        List<UserLogin> userLogins = userLoginRepo.findByParty(party);
        if (userLogins != null && userLogins.size() > 0) {
            return userLogins.get(0);
        }
        return null;
    }

    @Override
    public PartySalesman findById(UUID partyId) {

        return partySalesmanRepo.findByPartyId(partyId);
    }

    @Override
    public Person findPersonByPartyId(UUID partyId) {

        return personRepo.findByPartyId(partyId);
    }

    @Override
    public PartySalesman save(PersonModel salesman) {
        // TODO Auto-generated method stub
        try {
            Party party = userService.save(salesman);
            PartySalesman partySalesman = new PartySalesman();
            partySalesman.setPartyId(party.getPartyId());
            return partySalesmanRepo.save(partySalesman);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PartySalesman> findAll() {

        return partySalesmanRepo.findAll();
    }

}
