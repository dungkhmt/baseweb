package com.hust.baseweb.applications.salesroutes.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.salesroutes.entity.SalesmanCheckinHistory;
import com.hust.baseweb.entity.UserLogin;

@Service
public interface SalesmanCheckinHistoryService {
	SalesmanCheckinHistory save(UserLogin userLogin, UUID partyCustomerId, String checkinAction, String location);
}
