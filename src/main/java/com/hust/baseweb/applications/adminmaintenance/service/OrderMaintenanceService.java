package com.hust.baseweb.applications.adminmaintenance.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderMaintenanceService {
    int deleteOrder(String orderId);
}
