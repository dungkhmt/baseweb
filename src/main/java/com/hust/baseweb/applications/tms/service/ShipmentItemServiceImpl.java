package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import com.hust.baseweb.applications.tms.repo.*;
import com.hust.baseweb.utils.LatLngUtils;
import com.hust.baseweb.utils.PageUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShipmentItemServiceImpl implements ShipmentItemService {

    private ShipmentItemDeliveryPlanRepo shipmentItemDeliveryPlanRepo;

    private ShipmentItemRepo shipmentItemRepo;

    private ProductRepo productRepo;

    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryTripDetailRepo deliveryTripDetailRepo;
    private DistanceTravelTimeGeoPointRepo distanceTravelTimeGeoPointRepo;

    @Override
    public Page<ShipmentItemModel> findAllInDeliveryPlanId(String deliveryPlanId, Pageable pageable) {
        Page<ShipmentItemDeliveryPlan> shipmentItemDeliveryPlanPage
                = shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId), pageable);

        return new PageImpl<>(shipmentItemRepo.findAllByShipmentItemIdIn(
                shipmentItemDeliveryPlanPage.map(ShipmentItemDeliveryPlan::getShipmentItemId).getContent())
                .stream().map(ShipmentItem::toShipmentItemModel)
                .collect(Collectors.toList()),
                pageable,
                shipmentItemDeliveryPlanPage.getTotalElements()
        );
    }

    @Override
    public List<ShipmentItemModel> findAllInDeliveryPlanId(String deliveryPlanId) {
        List<ShipmentItemDeliveryPlan> shipmentItemDeliveryPlans = shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanId(
                UUID.fromString(deliveryPlanId));
        List<UUID> shipmentItemIds = shipmentItemDeliveryPlans.stream()
                .map(ShipmentItemDeliveryPlan::getShipmentItemId)
                .distinct()
                .collect(Collectors.toList());
        List<ShipmentItem> shipmentItems = shipmentItemRepo.findAllByShipmentItemIdIn(shipmentItemIds);
        return shipmentItems.stream().map(ShipmentItem::toShipmentItemModel).collect(Collectors.toList());
    }

    @Override
    public List<ShipmentItemModel.DeliveryPlan> findAllInDeliveryPlanIdNearestDeliveryTrip(String deliveryTripId) {
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(UUID.fromString(deliveryTripId))
                .orElseThrow(NoSuchElementException::new);
        DeliveryPlan deliveryPlan = deliveryTrip.getDeliveryPlan();
        String deliveryPlanId = deliveryPlan.getDeliveryPlanId().toString();
        List<DeliveryTrip> deliveryTripsInDeliveryPlan = deliveryTripRepo.findAllByDeliveryPlan(deliveryPlan);

        List<ShipmentItem> shipmentItemsInDeliveryPlan = shipmentItemRepo.findAllByShipmentItemIdIn(
                shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId)).stream()
                        .map(ShipmentItemDeliveryPlan::getShipmentItemId).collect(Collectors.toList()));

        List<ShipmentItem> shipmentItemsInDeliveryTrip
                = deliveryTripDetailRepo.findAllByDeliveryTrip(deliveryTrip)
                .stream().map(DeliveryTripDetail::getShipmentItem).collect(Collectors.toList());

        Map<ShipmentItem, Integer> shipmentItemAssignedQuantityMap = new HashMap<>();
        deliveryTripDetailRepo.findAllByDeliveryTripIn(deliveryTripsInDeliveryPlan)
                .forEach(deliveryTripDetail ->
                        shipmentItemAssignedQuantityMap.merge(deliveryTripDetail.getShipmentItem(),
                                deliveryTripDetail.getDeliveryQuantity(), Integer::sum));

        List<ShipmentItem> shipmentItemsNotInDeliveryTrip = shipmentItemsInDeliveryPlan.stream()
                .filter(shipmentItem ->
                        shipmentItemAssignedQuantityMap.getOrDefault(shipmentItem, 0) < shipmentItem.getQuantity())
                .collect(Collectors.toList());

        List<GeoPoint> geoPointsInDeliveryTrip = shipmentItemsInDeliveryTrip.stream()
                .map(shipmentItem -> shipmentItem.getShipToLocation().getGeoPoint())
                .distinct()
                .collect(Collectors.toList());
        List<GeoPoint> geoPointsNotInDeliveryTrip = shipmentItemsNotInDeliveryTrip.stream()
                .map(shipmentItem -> shipmentItem.getShipToLocation().getGeoPoint())
                .distinct()
                .collect(Collectors.toList());

        Map<GeoPoint, Double> geoPointToMinDistanceMap = new HashMap<>();
        for (GeoPoint geoPointNotIn : geoPointsNotInDeliveryTrip) {
            double minDistance = Double.MAX_VALUE;
            for (GeoPoint geoPointIn : geoPointsInDeliveryTrip) {
                DistanceTravelTimeGeoPoint distanceTravelTimeGeoPoint
                        = distanceTravelTimeGeoPointRepo.findByFromGeoPointIdAndToGeoPointId(geoPointIn.getGeoPointId(),
                        geoPointNotIn.getGeoPointId());
                double distance;
                if (distanceTravelTimeGeoPoint == null) {   // Haversine formula
                    distance = LatLngUtils.distance(
                            Double.parseDouble(geoPointNotIn.getLatitude()),
                            Double.parseDouble(geoPointNotIn.getLongitude()),
                            Double.parseDouble(geoPointIn.getLatitude()),
                            Double.parseDouble(geoPointIn.getLongitude())
                    );
                } else {
                    distance = distanceTravelTimeGeoPoint.getDistance();
                }
                if (distance < minDistance) {
                    minDistance = distance;
                    geoPointToMinDistanceMap.put(geoPointNotIn, minDistance);
                }
            }
        }

        return shipmentItemsNotInDeliveryTrip.stream()
                .sorted(Comparator.comparingDouble(o -> geoPointToMinDistanceMap.getOrDefault(o.getShipToLocation()
                        .getGeoPoint(), 0.0)))
                .map(shipmentItem -> shipmentItem.toShipmentItemDeliveryPlanModel(
                        shipmentItemAssignedQuantityMap.getOrDefault(shipmentItem, 0)))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ShipmentItemModel> findAllNotInDeliveryPlanId(String deliveryPlanId, Pageable pageable) {
        Set<String> shipmentItemInDeliveryPlans
                = shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId))
                .stream().map(shipmentItemDeliveryPlan -> shipmentItemDeliveryPlan.getShipmentItemId().toString())
                .collect(Collectors.toSet());
        List<ShipmentItem> allShipmentItems = new ArrayList<>();
        shipmentItemRepo.findAll().forEach(allShipmentItems::add);
        List<ShipmentItemModel> shipmentItemModels = allShipmentItems.stream()
                .filter(shipmentItem -> !shipmentItemInDeliveryPlans.contains(shipmentItem.getShipmentItemId()
                        .toString()))
                .map(ShipmentItem::toShipmentItemModel)
                .collect(Collectors.toList());
        return PageUtils.getPage(shipmentItemModels, pageable);
    }

    @Override
    public List<ShipmentItemModel> findAllNotInDeliveryPlanId(String deliveryPlanId) {
        Set<String> shipmentItemInDeliveryPlans
                = shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId))
                .stream().map(shipmentItemDeliveryPlan -> shipmentItemDeliveryPlan.getShipmentItemId().toString())
                .collect(Collectors.toSet());
        List<ShipmentItem> allShipmentItems = new ArrayList<>();
        shipmentItemRepo.findAll().forEach(allShipmentItems::add);
        return allShipmentItems.stream()
                .filter(shipmentItem -> !shipmentItemInDeliveryPlans.contains(shipmentItem.getShipmentItemId()
                        .toString()))
                .map(ShipmentItem::toShipmentItemModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ShipmentItem> findAll(Pageable pageable) {
        return shipmentItemRepo.findAll(pageable);
    }

    @Override
    public Iterable<ShipmentItem> findAll() {
        return shipmentItemRepo.findAll();
    }

    @Override
    public String saveShipmentItemDeliveryPlan(com.hust.baseweb.applications.tms.model.ShipmentItemModel.CreateDeliveryPlan createDeliveryPlan) {
        List<ShipmentItemDeliveryPlan> shipmentItemDeliveryPlans = new ArrayList<>();
        for (String shipmentItemId : createDeliveryPlan.getShipmentItemIds()) {
            shipmentItemDeliveryPlans.add(new ShipmentItemDeliveryPlan(
                    UUID.fromString(shipmentItemId),
                    UUID.fromString(createDeliveryPlan.getDeliveryPlanId())
            ));
        }
        shipmentItemDeliveryPlanRepo.saveAll(shipmentItemDeliveryPlans);
        return createDeliveryPlan.getDeliveryPlanId();
    }

    @Override
    public boolean deleteShipmentItemDeliveryPlan(ShipmentItemModel.DeleteDeliveryPlan deleteDeliveryPlan) {
        ShipmentItemDeliveryPlan shipmentItemDeliveryPlan = shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanIdAndShipmentItemId(
                UUID.fromString(deleteDeliveryPlan.getDeliveryPlanId()),
                UUID.fromString(deleteDeliveryPlan.getShipmentItemId())
        );
        if (shipmentItemDeliveryPlan != null) {
            shipmentItemDeliveryPlanRepo.delete(shipmentItemDeliveryPlan);
            return true;
        }
        return false;
    }


}
