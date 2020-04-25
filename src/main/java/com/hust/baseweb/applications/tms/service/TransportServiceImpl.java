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
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TransportServiceImpl implements TransportService {

    private TransportCustomerRepo transportCustomerRepo;
    private TransportDriverRepo transportDriverRepo;
    private TransportFacilityRepo transportFacilityRepo;

    @Override
    @Transactional
    public void updateTransportDeliveryTripDetailCompleted(DeliveryTripDetail... deliveryTripDetails) {
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
        transportCustomerRepo.saveAll(mergeData(transportCustomers, ids -> transportCustomerRepo.findAllByIdIn(ids)));

        // update driver report
        List<TransportDriver> transportDrivers = getTransportDrivers(deliveryTripDetailList,
                deliveryTripDetailToTransportReport);
        transportDriverRepo.saveAll(mergeData(transportDrivers, ids -> transportDriverRepo.findAllByIdIn(ids)));

        // update facility report
        List<TransportFacility> transportFacilities = getTransportFacilities(deliveryTripDetailList,
                deliveryTripDetailToTransportReport);
        transportFacilityRepo.saveAll(mergeData(transportFacilities, ids -> transportFacilityRepo.findAllByIdIn(ids)));
    }

    @Override
    public void updateTransportDeliveryTripCompleted(DeliveryTripDetail deliveryTripDetail) {
        if (deliveryTripDetail.getDeliveryTrip().getStatusItem().getStatusId().equals("DELIVERY_TRIP_COMPLETED")) {
            DeliveryTrip deliveryTrip = deliveryTripDetail.getDeliveryTrip();

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
                transportCustomerRepo.saveAll(mergeData(Collections.singletonList(transportCustomer),
                        ids -> transportCustomerRepo.findAllByIdIn(ids)));
            }
            if (transportDriver != null) {
                transportDriverRepo.saveAll(mergeData(Collections.singletonList(transportDriver),
                        ids -> transportDriverRepo.findAllByIdIn(ids)));
            }
            if (transportFacility != null) {
                transportFacilityRepo.saveAll(mergeData(Collections.singletonList(transportFacility),
                        ids -> transportFacilityRepo.findAllByIdIn(ids)));
            }
        }
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
        LocalDate localDate = deliveryTripDetail.getDeliveryTrip()
                .getExecuteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
        LocalDate localDate = deliveryTripDetail.getDeliveryTrip()
                .getExecuteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

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
        LocalDate localDate = deliveryTripDetail.getDeliveryTrip()
                .getExecuteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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

    private <ID, E extends TransportReport<ID>> Collection<E> mergeData(List<E> transportReports,
                                                                        Function<List<ID>, List<E>> getDBFunction) {
        Map<ID, E> onUpdateMap = transportReports.stream()
                .collect(Collectors.toMap(TransportReport::getId, e -> e, TransportReport::appendTo));

        Map<ID, E> inDBMap = getDBFunction.apply(new ArrayList<>(onUpdateMap.keySet())).stream()
                .collect(Collectors.toMap(TransportReport::getId, e -> e));

        onUpdateMap.forEach((id, transportReport) -> inDBMap.merge(id, transportReport, TransportReport::appendTo));

        return inDBMap.values();
    }
}
