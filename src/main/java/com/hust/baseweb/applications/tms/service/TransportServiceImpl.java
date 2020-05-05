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
import com.hust.baseweb.applications.tms.repo.TransportCustomerRepo;
import com.hust.baseweb.applications.tms.repo.TransportDriverRepo;
import com.hust.baseweb.applications.tms.repo.TransportFacilityRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Override
    public void updateTransportDeliveryTripDetailOnCompleted(DeliveryTripDetail... deliveryTripDetails) {
        List<DeliveryTripDetail> deliveryTripDetailList = Arrays.stream(deliveryTripDetails)
                .filter(deliveryTripDetail -> deliveryTripDetail.getStatusItem().getStatusId()
                        .equals("DELIVERY_TRIP_DETAIL_COMPLETED")) // chỉ cập nhật báo cáo cho các delivery trip detail đã hoàn thành
                .collect(Collectors.toList());

        Function<DeliveryTripDetail, TransportReport> deliveryTripDetailToTransportReport
                = deliveryTripDetail -> new TransportReport(
                0L,
                0,
                0,
                (int) (deliveryTripDetail.getDeliveryQuantity() *
                        deliveryTripDetail.getShipmentItem().getOrderItem().getProduct().getWeight() *
                        1000)) {
            @Override
            public Object getId() {
                return null;
            }
        };

        // update customer report
        List<TransportCustomer> transportCustomers = getTransportCustomers(deliveryTripDetailList,
                deliveryTripDetailToTransportReport);
        transportCustomerRepo.saveAll(mergeData(transportCustomers,
                ids -> transportCustomerRepo.findAllByIdIn(ids)).values());

        // update driver report
        List<TransportDriver> transportDrivers = getTransportDrivers(deliveryTripDetailList,
                deliveryTripDetailToTransportReport);
        transportDriverRepo.saveAll(mergeData(transportDrivers,
                ids -> transportDriverRepo.findAllByIdIn(ids)).values());

        // update facility report
        List<TransportFacility> transportFacilities = getTransportFacilities(deliveryTripDetailList,
                deliveryTripDetailToTransportReport);
        transportFacilityRepo.saveAll(mergeData(transportFacilities,
                ids -> transportFacilityRepo.findAllByIdIn(ids)).values());
    }

    @Override
    public void updateTransportDeliveryTripsOnCompleted(DeliveryTripDetail... deliveryTripDetails) {
        Map<DeliveryTrip, DeliveryTripDetail> deliveryTripToDetailMap = Arrays.stream(deliveryTripDetails)
                .filter(deliveryTripDetail -> deliveryTripDetail.getDeliveryTrip()
                        .getStatusItem()
                        .getStatusId()
                        .equals("DELIVERY_TRIP_COMPLETED"))
                .collect(Collectors.toMap(DeliveryTripDetail::getDeliveryTrip, e -> e, (one, two) -> one));

        Map<TransportCustomer.Id, TransportCustomer> transportCustomerMap = new HashMap<>();
        Map<TransportDriver.Id, TransportDriver> transportDriverMap = new HashMap<>();
        Map<TransportFacility.Id, TransportFacility> transportFacilityMap = new HashMap<>();

        deliveryTripToDetailMap.forEach((deliveryTrip, deliveryTripDetail) -> {
            TransportCustomer transportCustomer = Optional.ofNullable(getTransportCustomerId(deliveryTripDetail))
                    .map(id -> new TransportCustomer(id, 0L, deliveryTrip.getDistance().intValue(), 1, 0))
                    .orElse(null);
            TransportDriver transportDriver = Optional.ofNullable(getTransportDriverId(deliveryTripDetail))
                    .map(id -> new TransportDriver(id, 0L, deliveryTrip.getDistance().intValue(), 1, 0))
                    .orElse(null);
            TransportFacility transportFacility = Optional.ofNullable(getTransportFacilityId(deliveryTripDetail))
                    .map(id -> new TransportFacility(id, 0L, deliveryTrip.getDistance().intValue(), 1, 0))
                    .orElse(null);

            if (transportCustomer != null) {
                transportCustomerMap.put(transportCustomer.getId(), transportCustomer);
            }
            if (transportDriver != null) {
                transportDriverMap.put(transportDriver.getId(), transportDriver);
            }
            if (transportFacility != null) {
                transportFacilityMap.put(transportFacility.getId(), transportFacility);
            }
        });

        transportCustomerRepo.saveAll(mergeData(transportCustomerMap,
                ids -> transportCustomerRepo.findAllByIdIn(ids)).values());
        transportDriverRepo.saveAll(mergeData(transportDriverMap,
                ids -> transportDriverRepo.findAllByIdIn(ids)).values());
        transportFacilityRepo.saveAll(mergeData(transportFacilityMap,
                ids -> transportFacilityRepo.findAllByIdIn(ids)).values());
    }

    @Override
    public TransportReportModel.Output getTransportReports(TransportReportModel.Input input) {
        if (input.getCustomerId() != null) {
            List<TransportCustomer> transportCustomers = transportCustomerRepo.findAllById_CustomerIdAndId_DateBetween(
                    UUID.fromString(input.getCustomerId()),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_FORMAT).plusDays(1));
            List<TransportReportModel.DateReport> dateReports = transportCustomers.stream()
                    .map(TransportCustomer::toDateReport)
                    .collect(Collectors.toList());
            return new TransportReportModel.Output(dateReports);
        } else if (input.getDriverId() != null) {
            List<TransportDriver> transportDrivers = transportDriverRepo.findAllById_DriverIdAndId_DateBetween(
                    UUID.fromString(input.getDriverId()),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_FORMAT).plusDays(1));
            List<TransportReportModel.DateReport> dateReports = transportDrivers.stream()
                    .map(TransportDriver::toDateReport)
                    .collect(Collectors.toList());
            return new TransportReportModel.Output(dateReports);
        } else if (input.getFacilityId() != null) {
            List<TransportFacility> transportFacilities = transportFacilityRepo.findAllById_FacilityIdAndId_DateBetween(
                    input.getFacilityId(),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_FORMAT).plusDays(1));
            List<TransportReportModel.DateReport> dateReports = transportFacilities.stream()
                    .map(TransportFacility::toDateReport)
                    .collect(Collectors.toList());
            return new TransportReportModel.Output(dateReports);
        }
        return null;
    }

    @NotNull
    private List<TransportFacility> getTransportFacilities(List<DeliveryTripDetail> deliveryTripDetails,
                                                           Function<DeliveryTripDetail, TransportReport> deliveryTripDetailToTransportReport) {
        Function<DeliveryTripDetail, TransportFacility> deliveryTripDetailToTransportFacilityFunction =
                deliveryTripDetail -> {
                    TransportFacility.Id transportFacilityId = getTransportFacilityId(deliveryTripDetail);
                    if (transportFacilityId == null) {
                        return null;
                    }

                    TransportReport transportReport = deliveryTripDetailToTransportReport.apply(deliveryTripDetail);

                    return new TransportFacility(transportFacilityId,
                            transportReport.getCost(),
                            transportReport.getTotalDistance(),
                            transportReport.getNumberTrips(),
                            transportReport.getTotalWeight());
                };

        return deliveryTripDetails.stream()
                .map(deliveryTripDetailToTransportFacilityFunction)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    private TransportFacility.Id getTransportFacilityId(DeliveryTripDetail deliveryTripDetail) {
        String facilityId = Optional.ofNullable(deliveryTripDetail.getShipmentItem())
                .map(ShipmentItem::getFacility)
                .map(Facility::getFacilityId)
                .orElse(null);
        if (facilityId == null) {
            return null;
        }
        LocalDate localDate = LocalDate.now();

        return new TransportFacility.Id(facilityId, localDate);
    }

    @NotNull
    private List<TransportDriver> getTransportDrivers(List<DeliveryTripDetail> deliveryTripDetails,
                                                      Function<DeliveryTripDetail, TransportReport> deliveryTripDetailToTransportReport) {
        Function<DeliveryTripDetail, TransportDriver> deliveryTripDetailToTransportDriverFunction =
                deliveryTripDetail -> {
                    TransportDriver.Id transportDriverId = getTransportDriverId(deliveryTripDetail);
                    if (transportDriverId == null) {
                        return null;
                    }

                    TransportReport transportReport = deliveryTripDetailToTransportReport.apply(deliveryTripDetail);

                    return new TransportDriver(transportDriverId,
                            transportReport.getCost(),
                            transportReport.getTotalDistance(),
                            transportReport.getNumberTrips(),
                            transportReport.getTotalWeight());
                };

        return deliveryTripDetails.stream()
                .map(deliveryTripDetailToTransportDriverFunction)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    private TransportDriver.Id getTransportDriverId(DeliveryTripDetail deliveryTripDetail) {
        UUID driverId = Optional.ofNullable(deliveryTripDetail.getDeliveryTrip())
                .map(DeliveryTrip::getPartyDriver)
                .map(PartyDriver::getPartyId)
                .orElse(null);
        if (driverId == null) {
            return null;
        }
        LocalDate localDate = LocalDate.now();

        return new TransportDriver.Id(driverId, localDate);
    }

    @Nullable
    private TransportCustomer.Id getTransportCustomerId(DeliveryTripDetail deliveryTripDetail) {
        UUID customerId = Optional.ofNullable(deliveryTripDetail.getShipmentItem())
                .map(ShipmentItem::getPartyCustomer)
                .map(Party::getPartyId)
                .orElse(null);
        if (customerId == null) {
            return null;
        }
        LocalDate localDate = LocalDate.now();

        return new TransportCustomer.Id(customerId, localDate);
    }

    @NotNull
    private List<TransportCustomer> getTransportCustomers(List<DeliveryTripDetail> deliveryTripDetails,
                                                          Function<DeliveryTripDetail, TransportReport> deliveryTripDetailToTransportReport) {
        Function<DeliveryTripDetail, TransportCustomer> deliveryTripDetailToTransportCustomerFunction =
                deliveryTripDetail -> {
                    TransportCustomer.Id transportCustomerId = getTransportCustomerId(deliveryTripDetail);
                    if (transportCustomerId == null) {
                        return null;
                    }

                    TransportReport transportReport = deliveryTripDetailToTransportReport.apply(deliveryTripDetail);

                    return new TransportCustomer(transportCustomerId,
                            transportReport.getCost(),
                            transportReport.getTotalDistance(),
                            transportReport.getNumberTrips(),
                            transportReport.getTotalWeight());
                };

        return deliveryTripDetails.stream()
                .map(deliveryTripDetailToTransportCustomerFunction)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private <ID, E extends TransportReport<ID>> Map<ID, E> mergeData(List<E> transportReports,
                                                                     Function<List<ID>, List<E>> getDBFunction) {
        Map<ID, E> onUpdateMap = transportReports.stream()
                .collect(Collectors.toMap(TransportReport::getId, e -> e, TransportReport::appendTo));

        Map<ID, E> inDBMap = getDBFunction.apply(new ArrayList<>(onUpdateMap.keySet())).stream()
                .collect(Collectors.toMap(TransportReport::getId, e -> e));

        onUpdateMap.forEach((id, transportReport) -> inDBMap.merge(id, transportReport, TransportReport::appendTo));

        return inDBMap;
    }

    private <ID, E extends TransportReport<ID>> Map<ID, E> mergeData(Map<ID, E> sourceMap,
                                                                     Function<List<ID>, List<E>> getDBFunction) {
        Map<ID, E> inDBMap = getDBFunction.apply(new ArrayList<>(sourceMap.keySet())).stream()
                .collect(Collectors.toMap(TransportReport::getId, e -> e));

        sourceMap.forEach((id, transportReport) -> inDBMap.merge(id, transportReport, TransportReport::appendTo));

        return inDBMap;
    }

    private <ID, E extends TransportReport<ID>> void mergeData(List<E> transportReports,
                                                               Map<ID, E> sourceMap) {
        Map<ID, E> onUpdateMap = transportReports.stream()
                .collect(Collectors.toMap(TransportReport::getId, e -> e, TransportReport::appendTo));

        onUpdateMap.forEach((id, transportReport) -> sourceMap.merge(id, transportReport, TransportReport::appendTo));
    }
}
