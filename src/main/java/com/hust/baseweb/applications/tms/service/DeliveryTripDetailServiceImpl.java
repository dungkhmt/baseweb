package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.entity.status.DeliveryTripDetailStatus;
import com.hust.baseweb.applications.tms.entity.status.DeliveryTripStatus;
import com.hust.baseweb.applications.tms.entity.status.ShipmentItemStatus;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.DeliveryTripRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripDetailStatusRepo;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripStatusRepo;
import com.hust.baseweb.applications.tms.repo.status.ShipmentItemStatusRepo;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.repo.StatusItemRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
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

    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryTripService deliveryTripService;

    private StatusItemRepo statusItemRepo;

    private ShipmentItemStatusRepo shipmentItemStatusRepo;
    private DeliveryTripDetailStatusRepo deliveryTripDetailStatusRepo;
    private DeliveryTripStatusRepo deliveryTripStatusRepo;

    @Override
    public int save(String deliveryTripId,
                    List<DeliveryTripDetailModel.Create> inputs) {
        Date now = new Date();

        UUID deliveryTripIdUuid = UUID.fromString(deliveryTripId);
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(deliveryTripIdUuid)
                .orElseThrow(NoSuchElementException::new);

        StatusItem deliveryTripCreated = statusItemRepo.findById("DELIVERY_TRIP_CREATED")
                .orElseThrow(NoSuchElementException::new);
        if (!deliveryTrip.getStatusItem().equals(deliveryTripCreated)) {
            return 0;
        }

        Map<UUID, ShipmentItem> shipmentItemMap = buildShipmentItemMap(inputs);

        List<DeliveryTripDetail> deliveryTripDetails = new ArrayList<>();

        StatusItem shipmentItemCreated = statusItemRepo.findById("SHIPMENT_ITEM_CREATED")
                .orElseThrow(NoSuchElementException::new);
        StatusItem shipmentItemScheduledTripStatus = statusItemRepo.findById("SHIPMENT_ITEM_SCHEDULED_TRIP")
                .orElseThrow(NoSuchElementException::new);
        StatusItem deliveryTripDetailScheduledTripStatus = statusItemRepo.findById("DELIVERY_TRIP_DETAIL_SCHEDULED_TRIP")
                .orElseThrow(NoSuchElementException::new);

        Map<ShipmentItem, List<ShipmentItemStatus>> shipmentItemToStatusMap = shipmentItemStatusRepo.findAllByShipmentItemInAndThruDateNull(
                shipmentItemMap.values())
                .stream()
                .collect(Collectors.groupingBy(ShipmentItemStatus::getShipmentItem));

        for (DeliveryTripDetailModel.Create input : inputs) {
            log.info("save, input quantity = " + input.getDeliveryQuantity());
            DeliveryTripDetail deliveryTripDetail = new DeliveryTripDetail();
            deliveryTripDetail.setDeliveryTrip(deliveryTrip);

            ShipmentItem shipmentItem = shipmentItemMap.get(input.getShipmentItemId());

            if (!shipmentItem.getStatusItem().equals(shipmentItemCreated)) {
                continue;
            }

            log.info("save, find ShipmentItem " +
                    shipmentItem.getShipment().getShipmentId() +
                    "," +
                    shipmentItem.getShipmentItemId() +
                    ", product = " +
                    shipmentItem.getOrderItem().getProduct().getProductId());

            deliveryTripDetail.setShipmentItem(shipmentItem);
            deliveryTripDetail.setDeliveryQuantity(input.getDeliveryQuantity());
            deliveryTripDetail.setStatusItem(deliveryTripDetailScheduledTripStatus);

            shipmentItem.setScheduledQuantity(Math.min(
                    shipmentItem.getScheduledQuantity() + input.getDeliveryQuantity(), shipmentItem.getQuantity()));

            if (shipmentItem.getScheduledQuantity() == shipmentItem.getQuantity()) {
                shipmentItem.setStatusItem(shipmentItemScheduledTripStatus);

                List<ShipmentItemStatus> shipmentItemStatuses = shipmentItemToStatusMap.get(shipmentItem);
                for (ShipmentItemStatus shipmentItemStatus : shipmentItemStatuses) {
                    shipmentItemStatus.setThruDate(now);
                }
                shipmentItemStatuses.add(new ShipmentItemStatus(null,
                        shipmentItem,
                        shipmentItemScheduledTripStatus,
                        now,
                        null));
            }

            deliveryTripDetails.add(deliveryTripDetail);
        }

        deliveryTripDetailRepo.saveAll(deliveryTripDetails);

        deliveryTrip.setDeliveryTripDetailCount(deliveryTrip.getDeliveryTripDetailCount() + deliveryTripDetails.size());
        deliveryTripRepo.save(deliveryTrip);

        deliveryTripDetailStatusRepo.saveAll(deliveryTripDetails.stream()
                .map(deliveryTripDetail -> new DeliveryTripDetailStatus(null,
                        deliveryTripDetail,
                        deliveryTripDetailScheduledTripStatus,
                        now,
                        null,
                        null)).collect(Collectors.toList()));   // TODO: update user login id

        shipmentItemRepo.saveAll(shipmentItemMap.values()); // update scheduled quantity
        shipmentItemStatusRepo.saveAll(shipmentItemToStatusMap.values().stream().flatMap(Collection::stream).collect(
                Collectors.toList())); // convert List<List<T>> --> List<T>

        DeliveryTripModel.Tour deliveryTripInfo = deliveryTripService.getDeliveryTripInfo(deliveryTripId,
                new ArrayList<>());

        deliveryTrip = updateDeliveryTripInfo(deliveryTrip, deliveryTripInfo);

        updateDeliveryTripDetailSequence(deliveryTrip, deliveryTripInfo);

        return inputs.size();
    }

    @NotNull
    private DeliveryTrip updateDeliveryTripInfo(DeliveryTrip deliveryTrip, DeliveryTripModel.Tour deliveryTripInfo) {
        deliveryTrip.setTotalWeight(deliveryTripInfo.getTotalWeight());
        deliveryTrip.setTotalPallet(deliveryTripInfo.getTotalPallet());
        deliveryTrip.setDistance(deliveryTripInfo.getTotalDistance());
        deliveryTrip = deliveryTripRepo.save(deliveryTrip);
        return deliveryTrip;
    }

    @NotNull
    private Map<UUID, ShipmentItem> buildShipmentItemMap(List<DeliveryTripDetailModel.Create> inputs) {
        return shipmentItemRepo.findAllByShipmentItemIdIn(
                inputs.stream()
                        .map(DeliveryTripDetailModel.Create::getShipmentItemId)
                        .distinct()
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(ShipmentItem::getShipmentItemId, shipmentItem -> shipmentItem));
    }

    private void updateDeliveryTripDetailSequence(DeliveryTrip deliveryTrip, DeliveryTripModel.Tour deliveryTripInfo) {
        List<GeoPoint> tour = deliveryTripInfo.getTour();
        Map<GeoPoint, Integer> geoPointIndexMap = new HashMap<>();
        for (int i = 0; i < tour.size(); i++) {
            geoPointIndexMap.put(tour.get(i), i);
        }

        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTrip(deliveryTrip);
        for (DeliveryTripDetail deliveryTripDetail : deliveryTripDetails) {
            GeoPoint geoPoint = deliveryTripDetail.getShipmentItem().getShipToLocation().getGeoPoint();
            deliveryTripDetail.setSequenceId(geoPointIndexMap.get(geoPoint));
        }
        deliveryTripDetailRepo.saveAll(deliveryTripDetails);
    }

    @Override
    public boolean delete(String deliveryTripDetailId) {
        Date now = new Date();

        UUID deliveryTripDetailIdUuid = UUID.fromString(deliveryTripDetailId);
        DeliveryTripDetail deliveryTripDetail = deliveryTripDetailRepo.findById(deliveryTripDetailIdUuid)
                .orElseThrow(NoSuchElementException::new);

        StatusItem deliveryTripDetailScheduledTrip = statusItemRepo.findById("DELIVERY_TRIP_DETAIL_SCHEDULED_TRIP")
                .orElseThrow(NoSuchElementException::new);

        if (!deliveryTripDetail.getStatusItem().equals(deliveryTripDetailScheduledTrip)) {
            return false;
        }

        List<DeliveryTripDetailStatus> deliveryTripDetailStatuses = deliveryTripDetailStatusRepo.findAllByDeliveryTripDetail(
                deliveryTripDetail);
        deliveryTripDetailStatusRepo.deleteInBatch(deliveryTripDetailStatuses); // delete all history

        deliveryTripDetailRepo.deleteById(deliveryTripDetailIdUuid);

        DeliveryTrip deliveryTrip = deliveryTripDetail.getDeliveryTrip();

        DeliveryTripModel.Tour deliveryTripInfo = deliveryTripService.getDeliveryTripInfo(deliveryTrip.getDeliveryTripId()
                .toString(), new ArrayList<>());

        updateDeliveryTripInfo(deliveryTrip, deliveryTripInfo);

        updateShipmentItemStatus(now, deliveryTripDetail);

        // TODO: delivery trip status??

        updateDeliveryTripDetailSequence(deliveryTrip, deliveryTripInfo);

        return true;
    }

    private void updateShipmentItemStatus(Date updateDate, DeliveryTripDetail deliveryTripDetail) {
        // set shipment item status
        ShipmentItem shipmentItem = deliveryTripDetail.getShipmentItem();

        StatusItem shipmentItemCreated = statusItemRepo.findById("SHIPMENT_ITEM_CREATED")
                .orElseThrow(NoSuchElementException::new);
        StatusItem shipmentItemScheduledTrip = statusItemRepo.findById("SHIPMENT_ITEM_SCHEDULED_TRIP")
                .orElseThrow(NoSuchElementException::new);

        if (shipmentItem.getStatusItem().equals(shipmentItemScheduledTrip)) {
            shipmentItem.setStatusItem(shipmentItemCreated);
            List<ShipmentItemStatus> shipmentItemStatuses = shipmentItemStatusRepo.findAllByShipmentItemAndThruDateNull(
                    shipmentItem);
            for (ShipmentItemStatus shipmentItemStatus : shipmentItemStatuses) {
                shipmentItemStatus.setThruDate(updateDate);
            }
            shipmentItemStatuses.add(new ShipmentItemStatus(null,
                    shipmentItem,
                    shipmentItemCreated,
                    updateDate,
                    null));
            shipmentItemStatusRepo.saveAll(shipmentItemStatuses);
        }

        shipmentItem.setScheduledQuantity(shipmentItem.getScheduledQuantity() -
                deliveryTripDetail.getDeliveryQuantity());
        shipmentItemRepo.save(shipmentItem);
    }

    @Override
    public Page<DeliveryTripDetail> findAll(String deliveryTripId, Pageable pageable) {
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(UUID.fromString(deliveryTripId))
                .orElseThrow(NoSuchElementException::new);
        return deliveryTripDetailRepo.findAllByDeliveryTrip(deliveryTrip, pageable);
    }

    @Override
    public DeliveryTripDetailModel.OrderItems findAll(String deliveryTripId) {
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(UUID.fromString(deliveryTripId))
                .orElseThrow(NoSuchElementException::new);
        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTrip(deliveryTrip);
        if (deliveryTripDetails == null || deliveryTripDetails.isEmpty()) {
            return new DeliveryTripDetailModel.OrderItems(new ArrayList<>(), null, null);
        }
        GeoPoint facilityGeoPoint = deliveryTripDetails.get(0)
                .getShipmentItem()
                .getFacility()
                .getPostalAddress()
                .getGeoPoint();
        return new DeliveryTripDetailModel.OrderItems(
                deliveryTripDetails.stream()
                        .map(DeliveryTripDetail::toDeliveryTripDetailModel)
                        .collect(Collectors.toList()),
                facilityGeoPoint.getLatitude(),
                facilityGeoPoint.getLongitude()
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

    @Override
    public boolean completeDeliveryTripDetail(UUID deliveryTripDetailId) {
        Date now = new Date();

        StatusItem deliveryTripDetailCompleted = statusItemRepo.findById("DELIVERY_TRIP_DETAIL_COMPLETED")
                .orElseThrow(NoSuchElementException::new);
        StatusItem shipmentItemCompleted = statusItemRepo.findById("SHIPMENT_ITEM_COMPLETED")
                .orElseThrow(NoSuchElementException::new);
        StatusItem deliveryTripCompleted = statusItemRepo.findById("DELIVERY_TRIP_COMPLETED")
                .orElseThrow(NoSuchElementException::new);

        DeliveryTripDetail deliveryTripDetail = updateDeliveryTripDetailStatus(deliveryTripDetailId,
                now,
                deliveryTripDetailCompleted);

        if (deliveryTripDetail != null) {
            updateShipmentItemStatus(now, shipmentItemCompleted, deliveryTripDetail);

            updateDeliveryTrip(now, deliveryTripCompleted, deliveryTripDetail);

            return true;
        }

        return false;
    }

    private void updateDeliveryTrip(Date updateDate, StatusItem statusItem, DeliveryTripDetail deliveryTripDetail) {
        DeliveryTrip deliveryTrip = deliveryTripDetail.getDeliveryTrip();
        deliveryTrip.setCompletedDeliveryTripDetailCount(deliveryTrip.getCompletedDeliveryTripDetailCount() + 1);
        if (deliveryTrip.getCompletedDeliveryTripDetailCount().equals(deliveryTrip.getDeliveryTripDetailCount())) {
            deliveryTrip.setStatusItem(statusItem);
            deliveryTrip = deliveryTripRepo.save(deliveryTrip);
            List<DeliveryTripStatus> deliveryTripStatuses = deliveryTripStatusRepo.findAllByDeliveryTripAndThruDateNull(
                    deliveryTrip);
            deliveryTripStatuses.forEach(deliveryTripStatus -> deliveryTripStatus.setThruDate(updateDate));
            deliveryTripStatusRepo.saveAll(deliveryTripStatuses);

            deliveryTripStatusRepo.save(new DeliveryTripStatus(null, deliveryTrip, statusItem, updateDate, null));
        } else {
            deliveryTripRepo.save(deliveryTrip);
        }
    }

    private void updateShipmentItemStatus(Date updateDate,
                                          StatusItem statusItem,
                                          DeliveryTripDetail deliveryTripDetail) {
        ShipmentItem shipmentItem = deliveryTripDetail.getShipmentItem();
        shipmentItem.setCompletedQuantity(shipmentItem.getCompletedQuantity() +
                deliveryTripDetail.getDeliveryQuantity());
        if (shipmentItem.getCompletedQuantity() == shipmentItem.getQuantity()) {
            shipmentItem.setStatusItem(statusItem);
            shipmentItem = shipmentItemRepo.save(shipmentItem);
            List<ShipmentItemStatus> shipmentItemStatuses = shipmentItemStatusRepo.findAllByShipmentItemAndThruDateNull(
                    shipmentItem);
            shipmentItemStatuses.forEach(shipmentItemStatus -> shipmentItemStatus.setThruDate(updateDate));
            shipmentItemStatusRepo.saveAll(shipmentItemStatuses);

            shipmentItemStatusRepo.save(new ShipmentItemStatus(null, shipmentItem, statusItem, updateDate, null));
        } else {
            shipmentItemRepo.save(shipmentItem);
        }
    }

    private DeliveryTripDetail updateDeliveryTripDetailStatus(UUID deliveryTripDetailId,
                                                              Date updateDate,
                                                              StatusItem statusItem) {
        DeliveryTripDetail deliveryTripDetail = deliveryTripDetailRepo.findById(deliveryTripDetailId)
                .orElseThrow(NoSuchElementException::new);
        if (deliveryTripDetail.getStatusItem().getStatusId().equals(statusItem.getStatusId())) {
            return null;
        }
        deliveryTripDetail.setStatusItem(statusItem);
        deliveryTripDetailRepo.save(deliveryTripDetail);

        List<DeliveryTripDetailStatus> deliveryTripDetailStatuses = deliveryTripDetailStatusRepo.findAllByDeliveryTripDetailAndThruDateNull(
                deliveryTripDetail);
        deliveryTripDetailStatuses.forEach(deliveryTripDetailStatus -> deliveryTripDetailStatus.setThruDate(updateDate));
        deliveryTripDetailStatusRepo.saveAll(deliveryTripDetailStatuses);

        deliveryTripDetailStatusRepo.save(new DeliveryTripDetailStatus(null,
                deliveryTripDetail,
                statusItem,
                updateDate,
                null,
                null));
        return deliveryTripDetail;
    }

}
