package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PersonRepo;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class PartySalesmanServiceImpl implements PartySalesmanService {
    @Autowired
    private PartySalesmanRepo partySalesmanRepo;

    @Autowired
    private UserLoginRepo userLoginRepo;

    @Autowired
    private PartyRepo partyRepo;

    @Autowired
    private PersonRepo personRepo;

    @Override
    public List<GetSalesmanOutputModel> findAllSalesman() {
        // TODO Auto-generated method stub
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
            log.info("findAllSalesman, GOT userlogin " + (userLogin != null ? userLogin.getUserLoginId() : " NULL"));
            GetSalesmanOutputModel smm = new GetSalesmanOutputModel();
            smm.setPartySalesman(sm);
            smm.setUserLoginId(userLogin.getUserLoginId());
            retList.add(smm);
        }
        return retList;
    }

    @Override
    public UserLogin findUserLoginOfSalesmanId(UUID partySalesmanId) {
        // TODO Auto-generated method stub
        Party party = partyRepo.findByPartyId(partySalesmanId);
        List<UserLogin> userLogins = userLoginRepo.findByParty(party);
        if (userLogins != null && userLogins.size() > 0) {
            return userLogins.get(0);
        }
        return null;
    }

    @Override
    public PartySalesman findById(UUID partyId) {
        // TODO Auto-generated method stub
        return partySalesmanRepo.findByPartyId(partyId);
    }

    @Override
    public Person findPersonByPartyId(UUID partyId) {
        // TODO Auto-generated method stub
        return personRepo.findByPartyId(partyId);
    }

}
