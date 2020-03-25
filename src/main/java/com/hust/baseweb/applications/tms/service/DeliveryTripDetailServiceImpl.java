package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.DeliveryTripRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.repo.StatusItemRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@Transactional
public class DeliveryTripDetailServiceImpl implements DeliveryTripDetailService {

    private DeliveryTripDetailRepo deliveryTripDetailRepo;
    private ShipmentItemRepo shipmentItemRepo;
    private ProductRepo productRepo;

    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryTripService deliveryTripService;

    private StatusItemRepo statusItemRepo;


    @Override
    public int save(String deliveryTripId,
                    List<com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel.Create> inputs) {
        UUID deliveryTripIdUuid = UUID.fromString(deliveryTripId);
        for (com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel.Create input : inputs) {
            log.info("save, input quantity = " + input.getDeliveryQuantity());
            DeliveryTripDetail deliveryTripDetail = new DeliveryTripDetail();
            deliveryTripDetail.setDeliveryTripId(deliveryTripIdUuid);
            //deliveryTripDetail.setShipmentItem(input.getShipmentId());
            //deliveryTripDetail.setS
            ShipmentItem shipmentItem = shipmentItemRepo.findByShipmentItemId(input.getShipmentItemId());

            log.info("save, find ShipmentItem " +
                    shipmentItem.getShipment().getShipmentId() +
                    "," +
                    shipmentItem.getShipmentItemId() +
                    ", product = " +
                    shipmentItem.getOrderItem().getProduct().getProductId());

            deliveryTripDetail.setShipmentItem(shipmentItem);
            deliveryTripDetail.setDeliveryQuantity(input.getDeliveryQuantity());

            deliveryTripDetailRepo.save(deliveryTripDetail);
        }
        // TODO: reuse calc weight+distance+pallet function
        DeliveryTripModel.Tour deliveryTripInfo = deliveryTripService.getDeliveryTripInfo(deliveryTripId,
                new ArrayList<>());
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(deliveryTripIdUuid)
                .orElseThrow(NoSuchElementException::new);
        deliveryTrip.setTotalWeight(deliveryTripInfo.getTotalWeight());
        deliveryTrip.setTotalPallet(deliveryTripInfo.getTotalPallet());
        deliveryTrip.setDistance(deliveryTripInfo.getTotalDistance());
        deliveryTripRepo.save(deliveryTrip);

        updateDeliveryTripDetailSequence(deliveryTripIdUuid, deliveryTripInfo);

        return inputs.size();
    }

    public void updateDeliveryTripDetailSequence(UUID deliveryTripIdUuid, DeliveryTripModel.Tour deliveryTripInfo) {
        List<GeoPoint> tour = deliveryTripInfo.getTour();
        Map<GeoPoint, Integer> geoPointIndexMap = new HashMap<>();
        for (int i = 0; i < tour.size(); i++) {
            geoPointIndexMap.put(tour.get(i), i);
        }

        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTripId(deliveryTripIdUuid);
        for (DeliveryTripDetail deliveryTripDetail : deliveryTripDetails) {
            GeoPoint geoPoint = deliveryTripDetail.getShipmentItem().getShipToLocation().getGeoPoint();
            deliveryTripDetail.setSequence(geoPointIndexMap.get(geoPoint));
        }
        deliveryTripDetailRepo.saveAll(deliveryTripDetails);
    }

    @Override
    public boolean delete(String deliveryTripDetailId) {
        UUID deliveryTripDetailIdUuid = UUID.fromString(deliveryTripDetailId);
        DeliveryTripDetail deliveryTripDetail = deliveryTripDetailRepo.findById(deliveryTripDetailIdUuid)
                .orElseThrow(NoSuchElementException::new);
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(deliveryTripDetail.getDeliveryTripId())
                .orElseThrow(NoSuchElementException::new);
        deliveryTripDetailRepo.deleteById(deliveryTripDetailIdUuid);
        DeliveryTripModel.Tour deliveryTripInfo = deliveryTripService.getDeliveryTripInfo(deliveryTrip.getDeliveryTripId()
                .toString(), new ArrayList<>());
        deliveryTrip.setTotalWeight(deliveryTripInfo.getTotalWeight());
        deliveryTrip.setTotalPallet(deliveryTripInfo.getTotalPallet());
        deliveryTrip.setDistance(deliveryTripInfo.getTotalDistance());
        deliveryTripRepo.save(deliveryTrip);

        updateDeliveryTripDetailSequence(deliveryTrip.getDeliveryTripId(), deliveryTripInfo);

        return true;
    }

    @Override
    public Page<DeliveryTripDetail> findAll(String deliveryTripId, Pageable pageable) {
        return deliveryTripDetailRepo.findAllByDeliveryTripId(UUID.fromString(deliveryTripId), pageable);
    }

    @Override
    public DeliveryTripDetailModel.OrderItems findAll(String deliveryTripId) {
        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTripId(UUID.fromString(
                deliveryTripId));
        GeoPoint facilityGeoPoint = deliveryTripDetails.get(0)
                .getShipmentItem()
                .getOrderItem()
                .getFacility()
                .getPostalAddress()
                .getGeoPoint();
        return new DeliveryTripDetailModel.OrderItems(
                deliveryTripDetails.stream()
                        .map(deliveryTripDetail -> deliveryTripDetail.toDeliveryTripDetailModel(
                                deliveryTripDetail.getShipmentItem().getOrderItem().getProduct()))
                        .collect(Collectors.toList()),
                Double.parseDouble(facilityGeoPoint.getLatitude()),
                Double.parseDouble(facilityGeoPoint.getLongitude())
        );

    }

    @Override
    @Transactional
    public DeliveryTripDetail updateStatusDeliveryTripDetail(
            UUID deliveryTripDetailId, String statusId) {
        log.info("updateStatusDeliveryTripDetail, deliveryTripDetailId = " +
                deliveryTripDetailId +
                ", statusId = " +
                statusId);
        StatusItem statusItem = statusItemRepo.findByStatusId(statusId);
        DeliveryTripDetail dtd = deliveryTripDetailRepo.findByDeliveryTripDetailId(deliveryTripDetailId);
        if (dtd == null) {
            return null;
        }
        dtd.setStatusItem(statusItem);
        dtd = deliveryTripDetailRepo.save(dtd);
        log.info("updateStatusDeliveryTripDetail, deliveryTripDetailId = " +
                deliveryTripDetailId +
                ", statusId = " +
                statusId +
                " DONE");

        return dtd;
    }

}
