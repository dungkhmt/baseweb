package com.hust.baseweb.applications.humanresource.service;

import com.hust.baseweb.applications.humanresource.entity.Department;
import com.hust.baseweb.applications.humanresource.entity.PartyDepartment;
import com.hust.baseweb.applications.humanresource.repo.DepartmentRepo;
import com.hust.baseweb.applications.humanresource.repo.PartyDepartmentRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.repo.PartyRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PartyDepartmentServiceImpl implements PartyDepartmentService {

    private PartyDepartmentRepo partyDepartmentRepo;
    private DepartmentRepo departmentRepo;
    private PartyRepo partyRepo;

    @Override
    public PartyDepartment save(UUID partyId, String departmentId) {
        Department dept = departmentRepo.findByDepartmentId(departmentId);
        Party party = partyRepo.findByPartyId(partyId);

        List<PartyDepartment> lst = partyDepartmentRepo.findByPartyAndThruDate(party, null);

        for(PartyDepartment pd: lst){
            Date thruDate = new Date();// get current date-time
            pd.setThruDate(thruDate);
            partyDepartmentRepo.save(pd);
        }

        PartyDepartment partyDepartment = new PartyDepartment();
        partyDepartment.setParty(party);
        partyDepartment.setDepartment(dept);

        Date fromDate = new Date();// get current date-time
        partyDepartment.setFromDate(fromDate);

        partyDepartment = partyDepartmentRepo.save(partyDepartment);

        return partyDepartment;
    }
}
