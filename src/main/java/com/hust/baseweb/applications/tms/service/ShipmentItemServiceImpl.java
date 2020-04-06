package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.geo.embeddable.DistanceTravelTimePostalAddressEmbeddableId;
import com.hust.baseweb.applications.geo.entity.DistanceTravelTimePostalAddress;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.DistanceTravelTimePostalAddressRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.DeliveryTripRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentItemDeliveryPlanRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
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

    private DistanceTravelTimePostalAddressRepo distanceTraveltimePostalAddressRepo;

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

        List<PostalAddress> postalAddressInDeliveryTrip = shipmentItemsInDeliveryTrip.stream()
                .map(ShipmentItem::getShipToLocation)
                .distinct()
                .collect(Collectors.toList());
        List<PostalAddress> postalAddressNotInDeliveryTrip = shipmentItemsNotInDeliveryTrip.stream()
                .map(ShipmentItem::getShipToLocation)
                .distinct()
                .collect(Collectors.toList());

        // TODO
        Map<PostalAddress, Integer> postalAddressToMinDistanceMap = new HashMap<>();
        for (PostalAddress postalAddressNotIn : postalAddressNotInDeliveryTrip) {
            int minDistance = Integer.MAX_VALUE;
            for (PostalAddress postalAddressIn : postalAddressInDeliveryTrip) {
                DistanceTravelTimePostalAddress distanceTravelTimePostalAddress = distanceTraveltimePostalAddressRepo
                        .findByDistanceTravelTimePostalAddressEmbeddableId(
                                new DistanceTravelTimePostalAddressEmbeddableId(postalAddressIn.getContactMechId(),
                                        postalAddressNotIn.getContactMechId()));
                int distance;
                if (distanceTravelTimePostalAddress == null) {   // Haversine formula
                    distance = Integer.MAX_VALUE;
                } else {
                    distance = distanceTravelTimePostalAddress.getDistance();
                }
                if (distance < minDistance) {
                    minDistance = distance;
                    postalAddressToMinDistanceMap.put(postalAddressNotIn, minDistance);
                }
            }
        }

        return shipmentItemsNotInDeliveryTrip.stream()
                .sorted(Comparator.comparingDouble(o -> postalAddressToMinDistanceMap.getOrDefault(
                        o.getShipToLocation(), 0)))
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
        List<ShipmentItem> allShipmentItems = shipmentItemRepo.findAll();
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
        List<ShipmentItem> allShipmentItems = shipmentItemRepo.findAll();
        return allShipmentItems.stream()
                .filter(shipmentItem -> shipmentItem.getStatusItem().getStatusId().equals("SHIPMENT_ITEM_CREATED")
                        && !shipmentItemInDeliveryPlans.contains(shipmentItem.getShipmentItemId().toString()))
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
