package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.order.service.PartyCustomerService;
import com.hust.baseweb.applications.salesroutes.entity.SalesmanCheckinHistory;
import com.hust.baseweb.applications.salesroutes.repo.SalesmanCheckinHistoryRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesmanCheckinHistoryServiceImpl implements
    SalesmanCheckinHistoryService {

    private SalesmanCheckinHistoryRepo salesmanCheckinHistoryRepo;
    private PartyCustomerService partyCustomerService;
    private PartyCustomerRepo partyCustomerRepo;
    private PartyRepo partyRepo;

    @Override
    public SalesmanCheckinHistory save(UserLogin userLogin, UUID partyId, String checkinAction, String location) {

        Party party = partyRepo.findByPartyId(partyId);

        SalesmanCheckinHistory sch = new SalesmanCheckinHistory();
        //sch.setUserLogin(userLogin);
        sch.setUserLoginId(userLogin.getUserLoginId());
        sch.setParty(party);
        sch.setTimePoint(new Date());
        sch.setCheckinAction(checkinAction);
        sch.setLocation(location);
        sch = salesmanCheckinHistoryRepo.save(sch);
        return sch;
    }

    @Override
    public Page<SalesmanCheckinHistory> findAll(Pageable page) {

        return salesmanCheckinHistoryRepo.findAll(page);
    }

}
