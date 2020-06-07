package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DeliveryTripDetailService {

    int save(
        String deliveryTripId,
        List<DeliveryTripDetailModel.Create> inputs,
        UserLogin userLogin
    );

    boolean delete(String deliveryTripDetailId, UserLogin userLogin);

    Page<DeliveryTripDetail> findAll(String deliveryTripId, Pageable pageable);

    DeliveryTripDetailModel.OrderItems findAll(String deliveryTripId);

    DeliveryTripDetail updateStatusDeliveryTripDetail(UUID deliveryTripDetailId, String statusId);

    boolean completeDeliveryTripDetail(UUID... deliveryTripDetailIds);
}
