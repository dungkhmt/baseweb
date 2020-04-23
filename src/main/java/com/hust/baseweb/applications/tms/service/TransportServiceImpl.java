package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.document.aggregation.TransportCustomer;
import com.hust.baseweb.applications.tms.document.aggregation.TransportDriver;
import com.hust.baseweb.applications.tms.document.aggregation.TransportFacility;
import com.hust.baseweb.applications.tms.document.aggregation.TransportReport;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.TransportCustomerRepo;
import com.hust.baseweb.applications.tms.repo.TransportDriverRepo;
import com.hust.baseweb.applications.tms.repo.TransportFacilityRepo;
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

    @Override
    public void updateTransport(DeliveryTrip... deliveryTrips) {
        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTripIn(Arrays.asList(
                deliveryTrips));

        updateTransportCustomer(deliveryTripDetails);

        updateTransportDriver(deliveryTripDetails);

        updateTransportFacility(deliveryTripDetails);
    }

    private void updateTransportFacility(List<DeliveryTripDetail> deliveryTripDetails) {
        Function<DeliveryTripDetail, TransportFacility> deliveryTripDetailToTransportFacilityFunction =
                deliveryTripDetail -> {
                    DeliveryTrip deliveryTrip = deliveryTripDetail.getDeliveryTrip();

                    String facilityId = deliveryTripDetail.getShipmentItem().getFacility().getFacilityId();
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

                    UUID driverId = deliveryTripDetail.getDeliveryTrip().getPartyDriver().getPartyId();
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
                    UUID customerId = deliveryTripDetail.getShipmentItem().getPartyCustomer().getPartyId();
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

            onUpdateMap.merge(transportReport.getId(), transportReport, TransportReport::appendTo);
        }

        Map<ID, E> inDBMap = getDBFunction.apply(new ArrayList<>(onUpdateMap.keySet()))
                .stream().collect(Collectors.toMap(TransportReport::getId, transportReport -> transportReport));

        onUpdateMap.forEach((id, transportReport) -> inDBMap.merge(id, transportReport, TransportReport::appendTo));

        return inDBMap.values();
    }
}
