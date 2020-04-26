package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.model.TransportReportModel;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportService {

    void updateTransportDeliveryTripDetailOnCompleted(DeliveryTripDetail... deliveryTripDetails);

    void updateTransportDeliveryTripsOnCompleted(DeliveryTripDetail... deliveryTripDetails);

    TransportReportModel.Output getTransportReports(TransportReportModel.Input input);
}
