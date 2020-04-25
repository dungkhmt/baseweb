package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.model.TransportReportModel;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportService {

    void updateTransportDeliveryTripDetailCompleted(DeliveryTripDetail... deliveryTripDetails);

    void updateTransportDeliveryTripCompleted(DeliveryTripDetail deliveryTripDetail);

    TransportReportModel.Output getTransportReports(TransportReportModel.Input input);
}
