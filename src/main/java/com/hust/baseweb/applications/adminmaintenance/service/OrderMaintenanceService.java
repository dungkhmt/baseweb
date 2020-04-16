package com.hust.baseweb.applications.adminmaintenance.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderMaintenanceService {
    public int deleteOrder(String orderId);
}
