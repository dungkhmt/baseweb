package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesmanCheckinHistory;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SalesmanCheckinHistoryService {
    SalesmanCheckinHistory save(UserLogin userLogin, UUID partyCustomerId, String checkinAction, String location);

    Page<SalesmanCheckinHistory> findAll(Pageable page);
}
