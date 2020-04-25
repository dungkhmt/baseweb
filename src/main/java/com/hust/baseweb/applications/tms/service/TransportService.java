package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.model.TransportReportModel;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportService {

    void updateTransport(DeliveryTripDetail... deliveryTripDetails);

    void updateTransport(DeliveryTrip... deliveryTrips);

    TransportReportModel.Output getTransportReports(TransportReportModel.Input input);
}
