package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.tms.document.aggregation.TransportCustomer;
import com.hust.baseweb.applications.tms.document.aggregation.TransportDriver;
import com.hust.baseweb.applications.tms.document.aggregation.TransportFacility;
import com.hust.baseweb.applications.tms.document.aggregation.TransportReport;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.TransportReportModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.TransportCustomerRepo;
import com.hust.baseweb.applications.tms.repo.TransportDriverRepo;
import com.hust.baseweb.applications.tms.repo.TransportFacilityRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.repo.StatusItemRepo;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class TransportServiceImpl implements TransportService {

    private TransportCustomerRepo transportCustomerRepo;
    private TransportDriverRepo transportDriverRepo;
    private TransportFacilityRepo transportFacilityRepo;

    private DeliveryTripDetailRepo deliveryTripDetailRepo;

    private StatusItemRepo statusItemRepo;

    @Override
    public void updateTransport(DeliveryTripDetail... deliveryTripDetails) {
        List<DeliveryTripDetail> deliveryTripDetailList = Arrays.stream(deliveryTripDetails)
                .filter(deliveryTripDetail -> deliveryTripDetail.getStatusItem().getStatusId()
                        .equals("DELIVERY_TRIP_DETAIL_COMPLETED")) // chỉ cập nhật báo cáo cho các delivery trip detail đã hoàn thành
                .collect(Collectors.toList());

        updateTransportCustomer(deliveryTripDetailList);

        updateTransportDriver(deliveryTripDetailList);

        updateTransportFacility(deliveryTripDetailList);
    }

    @Override
    public void updateTransport(DeliveryTrip... deliveryTrips) {
        StatusItem deliveryTripDetailCompleted = statusItemRepo.findById("DELIVERY_TRIP_DETAIL_COMPLETED")
                .orElseThrow(NoSuchElementException::new);
        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTripInAndStatusItem(
                Arrays.asList(deliveryTrips),
                deliveryTripDetailCompleted); // chỉ cập nhật báo cáo cho các delivery trip detail đã hoàn thành

        updateTransportCustomer(deliveryTripDetails);

        updateTransportDriver(deliveryTripDetails);

        updateTransportFacility(deliveryTripDetails);
    }

    @Override
    public TransportReportModel.Output getTransportReports(TransportReportModel.Input input) {
        if (input.getCustomerId() != null) {
            List<TransportCustomer> transportCustomers = transportCustomerRepo.findAllById_CustomerIdAndId_DateBetween(
                    UUID.fromString(input.getCustomerId()),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_FORMAT));
            List<TransportReportModel.DateReport> dateReports = transportCustomers.stream()
                    .map(TransportCustomer::toDateReport)
                    .collect(Collectors.toList());
            return new TransportReportModel.Output(dateReports);
        } else if (input.getDriverId() != null) {
            List<TransportDriver> transportDrivers = transportDriverRepo.findAllById_DriverIdAndId_DateBetween(
                    UUID.fromString(input.getDriverId()),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_FORMAT));
            List<TransportReportModel.DateReport> dateReports = transportDrivers.stream()
                    .map(TransportDriver::toDateReport)
                    .collect(Collectors.toList());
            return new TransportReportModel.Output(dateReports);
        } else if (input.getFacilityId() != null) {
            List<TransportFacility> transportFacilities = transportFacilityRepo.findAllById_FacilityIdAndId_DateBetween(
                    input.getFacilityId(),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_FORMAT));
            List<TransportReportModel.DateReport> dateReports = transportFacilities.stream()
                    .map(TransportFacility::toDateReport)
                    .collect(Collectors.toList());
            return new TransportReportModel.Output(dateReports);
        }
        return null;
    }

    private void updateTransportFacility(List<DeliveryTripDetail> deliveryTripDetails) {
        Function<DeliveryTripDetail, TransportFacility> deliveryTripDetailToTransportFacilityFunction =
                deliveryTripDetail -> {
                    DeliveryTrip deliveryTrip = deliveryTripDetail.getDeliveryTrip();

                    String facilityId = Optional.ofNullable(deliveryTripDetail.getShipmentItem())
                            .map(ShipmentItem::getFacility)
                            .map(Facility::getFacilityId)
                            .orElse(null);
                    if (facilityId == null) {
                        return null;
                    }
                    LocalDate localDate = deliveryTrip
                            .getExecuteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    return new TransportFacility(new TransportFacility.Id(facilityId, localDate),
                            0L,
                            deliveryTrip.getDistance().intValue(),
                            1,
                            deliveryTrip.getTotalWeight().intValue() * 1000);
                };

        Collection<TransportFacility> transportFacilities = mergeData(deliveryTripDetails,
                deliveryTripDetailToTransportFacilityFunction,
                ids -> transportFacilityRepo.findAllByIdIn(ids));

        transportFacilityRepo.saveAll(transportFacilities);
    }

    private void updateTransportDriver(List<DeliveryTripDetail> deliveryTripDetails) {
        Function<DeliveryTripDetail, TransportDriver> deliveryTripDetailToTransportDriverFunction =
                deliveryTripDetail -> {
                    DeliveryTrip deliveryTrip = deliveryTripDetail.getDeliveryTrip();

                    UUID driverId = Optional.ofNullable(deliveryTripDetail.getDeliveryTrip())
                            .map(DeliveryTrip::getPartyDriver)
                            .map(PartyDriver::getPartyId)
                            .orElse(null);
                    if (driverId == null) {
                        return null;
                    }
                    LocalDate localDate = deliveryTrip
                            .getExecuteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return new TransportDriver(new TransportDriver.Id(driverId, localDate),
                            0L,
                            deliveryTrip.getDistance().intValue(),
                            1,
                            deliveryTrip.getTotalWeight().intValue() * 1000);
                };

        Collection<TransportDriver> transportDrivers = mergeData(deliveryTripDetails,
                deliveryTripDetailToTransportDriverFunction,
                ids -> transportDriverRepo.findAllByIdIn(ids));

        transportDriverRepo.saveAll(transportDrivers);
    }

    private void updateTransportCustomer(List<DeliveryTripDetail> deliveryTripDetails) {
        Function<DeliveryTripDetail, TransportCustomer> deliveryTripDetailToTransportCustomerFunction =
                deliveryTripDetail -> {
                    DeliveryTrip deliveryTrip = deliveryTripDetail.getDeliveryTrip();
                    UUID customerId = Optional.ofNullable(deliveryTripDetail.getShipmentItem())
                            .map(ShipmentItem::getPartyCustomer)
                            .map(Party::getPartyId)
                            .orElse(null);
                    if (customerId == null) {
                        return null;
                    }
                    LocalDate localDate = deliveryTrip
                            .getExecuteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return new TransportCustomer(new TransportCustomer.Id(customerId, localDate),
                            0L,
                            deliveryTrip.getDistance().intValue(),
                            1,
                            deliveryTrip.getTotalWeight().intValue() * 1000);
                };

        Collection<TransportCustomer> transportCustomers = mergeData(deliveryTripDetails,
                deliveryTripDetailToTransportCustomerFunction,
                ids -> transportCustomerRepo.findAllByIdIn(ids));

        transportCustomerRepo.saveAll(transportCustomers);
    }

    private <ID, E extends TransportReport<ID>> Collection<E> mergeData(List<DeliveryTripDetail> deliveryTripDetails,
                                                                        Function<DeliveryTripDetail, E> convertFunction,
                                                                        Function<List<ID>, List<E>> getDBFunction) {
        Map<ID, E> onUpdateMap = new HashMap<>();
        for (DeliveryTripDetail deliveryTripDetail : deliveryTripDetails) {
            E transportReport = convertFunction.apply(deliveryTripDetail);
            if (transportReport == null) {
                continue;
            }

            onUpdateMap.merge(transportReport.getId(), transportReport, TransportReport::appendTo);
        }

        Map<ID, E> inDBMap = getDBFunction.apply(new ArrayList<>(onUpdateMap.keySet()))
                .stream().collect(Collectors.toMap(TransportReport::getId, transportReport -> transportReport));

        onUpdateMap.forEach((id, transportReport) -> inDBMap.merge(id, transportReport, TransportReport::appendTo));

        return inDBMap.values();
    }
}
